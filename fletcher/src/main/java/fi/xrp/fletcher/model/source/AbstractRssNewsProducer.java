package fi.xrp.fletcher.model.source;

import com.google.common.base.Strings;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndImage;
import com.sun.syndication.io.SyndFeedInput;
import fi.xrp.fletcher.model.api.News;
import fi.xrp.fletcher.service.http.ResponseMapper;
import fi.xrp.fletcher.service.http.XmlResponseHandler;
import fi.xrp.fletcher.utility.BasicUtility;
import fi.xrp.fletcher.utility.TextUtility;
import fi.xrp.fletcher.utility.UrlUtility;
import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.Document;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

abstract class AbstractRssNewsProducer extends AbstractNewsProducer<Document> {
    AbstractRssNewsProducer(final Set<Tag> tags) {
        super(tags);
    }

    @Override
    protected List<News> extractNews(final Document object) {
        final List<News> news = new LinkedList<>();

        try {
            final SyndFeedInput input = new SyndFeedInput();
            object.normalizeDocument();
            final SyndFeed romeFeed = input.build(object);

            romeFeed.getEntries().forEach(entry -> {
                final SyndEntry rssFeedEntry = (SyndEntry) entry;

                final String guid = getGuid(rssFeedEntry);

                if (shouldIncludeEntry(rssFeedEntry)) {
                    news.add(getNews(guid, romeFeed, rssFeedEntry));
                }
            });
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

        return news;
    }

    @Override
    protected ResponseMapper<Document> getResponseMapper() {
        return new XmlResponseHandler();
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

    private boolean shouldIncludeEntry(final SyndEntry rssFeedEntry) {
        return !tags.contains(Tag.NEEDS_FILTERING) || hasKeyword(rssFeedEntry.getTitle());
    }

    protected News getNews(final String guid, final SyndFeed rssFeed, final SyndEntry rssFeedEntry) {
        final News news = new News();

        final String sourceTitleFromFeed = contentToText(rssFeed.getTitleEx());
        final String sourceTitle = Strings.isNullOrEmpty(sourceTitleFromFeed) ? getTitle() : sourceTitleFromFeed;

        news.setDate(rssFeedEntry.getPublishedDate().getTime());
        news.setGuid(guid);
        news.setTitle(contentToText(rssFeedEntry.getTitleEx()));
        news.setSourceName(sourceTitle);
        news.setUrl(rssFeedEntry.getLink());
        news.setSourceFeedUrl(getFeedUrl());
        news.setSourceHomeUrl(getHomeUrl());
        news.setSourceImageUrl(getFeedSourceImage(rssFeed));

        final String bodyHtml = contentToText(rssFeedEntry);
        if (tags.contains(Tag.DO_NOT_CLEANUP)) {
            news.setBody(bodyHtml);
        } else {
            news.setBody(TextUtility.htmlCleanupRelaxed(bodyHtml));
        }

        final Set<String> imageUrls = UrlUtility.extractImageUrls(news.getBody());
        if (!imageUrls.isEmpty()) {
            news.setImageUrls(imageUrls);
        }

        final String homeHost = URI.create(getHomeUrl()).getHost();
        final Set<String> externalUrls = UrlUtility.getExternalUrls(bodyHtml, uri -> !Strings.isNullOrEmpty(uri.getHost()) && !uri.getHost().contains(homeHost));
        if (!externalUrls.isEmpty()) {
            news.setExternalUrls(externalUrls);
        }

        news.setTags(tags.stream().map(a -> a.name()).collect(Collectors.toSet()));

        if (news.getTags().contains(Tag.ALWAYS_IMPORTANT.name()) || isImportant(sourceTitle)) {
            news.setImportant(true);
        }

        if (news.getTags().contains(Tag.UNOFFICIAL.name())) {
            news.setCommunity(true);
        }

        return news;
    }

    private String getFeedSourceImage(final SyndFeed rssFeed) {
        final SyndImage image = rssFeed.getImage();
        if (image == null || Strings.isNullOrEmpty(image.getUrl())) {
            return null;
        }
        return image.getUrl();
    }

    private String contentToText(final SyndEntry rssFeedEntry) {
        if (rssFeedEntry.getContents() != null && !rssFeedEntry.getContents().isEmpty() && rssFeedEntry.getContents().get(0) instanceof SyndContent) {
            return contentToText((SyndContent) rssFeedEntry.getContents().get(0));
        } else if (rssFeedEntry.getDescription() != null) {
            return contentToText(rssFeedEntry.getDescription());
        } else {
            return null;
        }
    }

    private String contentToText(final SyndContent content) {
        final String value = content.getValue();
        if (Strings.isNullOrEmpty(value)) {
            return null;
        }
        return StringEscapeUtils.unescapeHtml4(value);
    }
}
