package fi.xrp.fletcher.model.source;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndImage;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import fi.xrp.fletcher.service.Clients;
import fi.xrp.fletcher.service.http.AbstractBasicXmlHandler;
import fi.xrp.fletcher.utility.BasicUtility;
import fi.xrp.fletcher.utility.TextUtility;
import fi.xrp.fletcher.utility.UrlUtility;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URI;
import java.util.Locale;
import java.util.Set;

public abstract class AbstractRssNewsProducer implements NewsProducer {
    private final Set<String> KEYWORDS = Sets.newHashSet(
            "xrp", "ripple", "swell", "xrapid", "xvia", "xcurrent", "swift", "ilp", "dlt"
    );

    private final Set<String> IMPORTANT_MARKERS = Sets.newHashSet(
            "launch", "launches", "launched", "launching", "breaking", "authorized", "authorised", "authorizes", "authorises", "authorize", "authorise"
    );

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final DocumentBuilder db;
    private final Set<Tag> tags;

    public AbstractRssNewsProducer(final Set<Tag> tags) {
        this.tags = tags;
        try {
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            logger.error("XML Parser error.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getTitle() {
        return getTitle(getHomeUrl());
    }

    @Override
    public String getHomeUrl() {
        return getFeedUrl();
    }

    @Override
    public void scheduleAsyncUpdate(final Clients clients, final NewsListener listener, final NewsGraph database) {
        listener.onUpdateStarted(this);

        clients.executeAsyncHttpGet(getFeedUrl(), new AbstractBasicXmlHandler() {
            @Override
            public void onSuccess(final Document document) throws Exception {
                listener.onUpdateCompleted(AbstractRssNewsProducer.this);

                final SyndFeedInput input = new SyndFeedInput();
                try {
                    final SyndFeed romeFeed = input.build(document);
                    romeFeed.getEntries().forEach(entry -> {
                        final SyndEntry rssFeedEntry = (SyndEntry) entry;
                        updateDatabase(clients, database, romeFeed, rssFeedEntry);
                    });
                } catch (FeedException e) {
                    logger.error("Cannot parse " + getFeedUrl(), e);
                    throw new IOException(e);
                } catch (Exception e) {
                    logger.error("Generic error " + getFeedUrl(), e);
                }
            }

            @Override
            public void onThrowable(final Throwable t) {
                super.onThrowable(t);
                listener.onUpdateFailed(AbstractRssNewsProducer.this, t);
            }
        });
    }

    private String getTitle(final String feedUrl) {
        if (Strings.isNullOrEmpty(feedUrl)) {
            return "N/A";
        }
        final URI uri = URI.create(feedUrl);
        String name;
        if ("feeds.feedburner.com".equalsIgnoreCase(uri.getHost())) {
            name = uri.getPath();
        } else {
            name = uri.getHost();
        }
        if (Strings.isNullOrEmpty(name)) {
            return feedUrl;
        }
        name = name.replace("www.", "");
        final int lastDotIndex = name.lastIndexOf('.');
        if (lastDotIndex != -1) {
            name = name.substring(0, lastDotIndex);
        }
        while (name.endsWith("/")) {
            name = name.substring(0, name.length() - 1);
        }
        while (name.startsWith("/")) {
            name = name.substring(1);
        }
        return name;
    }

    private String getGuid(final SyndEntry rssFeedEntry) {
        String uri = rssFeedEntry.getUri();
        uri = uri.trim();
        uri = uri.toUpperCase(Locale.ROOT);
        while (uri.endsWith("/")) {
            uri = uri.substring(0, uri.length() - 1);
        }
        return BasicUtility.hash(uri);
    }

    private void updateDatabase(final Clients clients, final NewsGraph database, final SyndFeed rssFeed, final SyndEntry rssFeedEntry) {
        final String guid = getGuid(rssFeedEntry);

        if (shouldUpdateDatabase(clients, database, guid, rssFeed, rssFeedEntry)) {
            updateDatabase(clients, database, guid, rssFeed, rssFeedEntry);
        }
    }

    private boolean shouldUpdateDatabase(final Clients clients, final NewsGraph database, final String guid, final SyndFeed rssFeed, final SyndEntry rssFeedEntry) {
        final Set<String> keywords = TextUtility.getKeywords(rssFeedEntry.getTitle());

        return !tags.contains(Tag.NEEDS_FILTERING) || applyFilter(keywords, rssFeedEntry.getTitle());
    }

    protected void updateDatabase(final Clients clients, final NewsGraph database, final String guid, final SyndFeed rssFeed, final SyndEntry rssFeedEntry) {
        final String sourceImageUrl = getFeedSourceImage(rssFeed);
        final String titleFromFeed = contentToText(rssFeed.getTitleEx());
        final String title = Strings.isNullOrEmpty(titleFromFeed) ? getTitle() : titleFromFeed;

        tags.forEach(tag -> database.attachTag(guid, tag));
        database.attachDates(guid, rssFeedEntry.getPublishedDate(), rssFeedEntry.getUpdatedDate());
        database.attachGenericSource(guid, title);
        database.attachTitle(guid, contentToText(rssFeedEntry.getTitleEx()));
        database.attachUrl(guid, rssFeedEntry.getLink(), getFeedUrl(), getHomeUrl(), sourceImageUrl);

        if (!Strings.isNullOrEmpty(sourceImageUrl)) {
            // database.attachAvatarUrl(guid, sourceImageUrl, sourceImageUrl, sourceImageUrl);
        }

        final String bodyHtml = contentToText(rssFeedEntry);
        if (tags.contains(Tag.DO_NOT_CLEANUP)) {
            database.attachBody(guid, bodyHtml);
        } else {
            database.attachBody(guid, TextUtility.htmlCleanupRelaxed(bodyHtml));
        }

        UrlUtility.extractImageUrls(bodyHtml).forEach(url -> database.attachImageUrl(guid, url));

        final Set<String> keywords = TextUtility.getKeywords(rssFeedEntry.getTitle());
        keywords.forEach(keyword -> database.attachKeyword(guid, keyword));

        if (tags.contains(Tag.ALWAYS_IMPORTANT) || !Sets.intersection(IMPORTANT_MARKERS, keywords).isEmpty()) {
            database.markImportant(guid);
        }

        if (tags.contains(Tag.UNOFFICIAL)) {
            database.markCommunity(guid);
        }

        final String homeHost = URI.create(getHomeUrl()).getHost();
        final Set<String> externalUrls = UrlUtility.getExternalUrls(bodyHtml, uri -> !Strings.isNullOrEmpty(uri.getHost()) && !uri.getHost().contains(homeHost));
        if (!externalUrls.isEmpty()) {
            externalUrls.forEach(url -> database.attachExternalUrl(guid, url));
        }
    }

    private String getFeedSourceImage(final SyndFeed rssFeed) {
        final SyndImage image = rssFeed.getImage();
        if (image == null || Strings.isNullOrEmpty(image.getUrl())) {
            return null;
        }
        return image.getUrl();
    }

    private boolean applyFilter(final Set<String> keywords, final String title) {
        if (!Sets.intersection(KEYWORDS, keywords).isEmpty()) {
            return true;
        }
        final String titleFixed = Strings.nullToEmpty(title).toLowerCase(Locale.ROOT);
        return KEYWORDS.stream().anyMatch(titleFixed::contains);
    }

    private static String contentToText(final SyndEntry rssFeedEntry) {
        if (rssFeedEntry.getContents() != null && !rssFeedEntry.getContents().isEmpty() && rssFeedEntry.getContents().get(0) instanceof SyndContent) {
            return contentToText((SyndContent) rssFeedEntry.getContents().get(0));
        } else if (rssFeedEntry.getDescription() != null) {
            return contentToText(rssFeedEntry.getDescription());
        } else {
            return null;
        }
    }

    private static String contentToText(final SyndContent content) {
        final String value = content.getValue();
        if (Strings.isNullOrEmpty(value)) {
            return null;
        }
        return StringEscapeUtils.unescapeHtml4(value);
    }
}
