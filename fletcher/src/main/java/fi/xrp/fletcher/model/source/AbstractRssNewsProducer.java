package fi.xrp.fletcher.model.source;

import com.google.common.base.Strings;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndImage;
import com.sun.syndication.io.SyndFeedInput;
import fi.xrp.fletcher.model.api.News;
import fi.xrp.fletcher.utility.BasicUtility;
import fi.xrp.fletcher.utility.TextUtility;
import fi.xrp.fletcher.utility.UrlUtility;
import org.apache.commons.lang3.StringEscapeUtils;
import org.asynchttpclient.Response;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
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
    protected Document mapResponse(final Response response) throws Exception {
        final DocumentBuilder documentBuilder = getDocumentBuilder();

        try (final InputStream inputStream = response.getResponseBodyAsStream()) {
            return documentBuilder.parse(inputStream);
        }
    }

    @Override
    protected List<News> mapFuture(final Document document) {
        final List<News> news = new LinkedList<>();

        try {
            final SyndFeedInput input = new SyndFeedInput();
            final SyndFeed romeFeed = input.build(document);

            romeFeed.getEntries().forEach(entry -> {
                final SyndEntry rssFeedEntry = (SyndEntry) entry;

                final String guid = getGuid(rssFeedEntry);

                if (shouldUpdateDatabase(rssFeedEntry)) {
                    news.add(getNews(guid, romeFeed, rssFeedEntry));
                }
            });
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

        return news;
    }

    protected DocumentBuilder getDocumentBuilder() throws Exception {
        return getDocumentBuilderFactory().newDocumentBuilder();
    }

    protected DocumentBuilderFactory getDocumentBuilderFactory() {
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setIgnoringComments(true);
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);
        documentBuilderFactory.setValidating(false);
        documentBuilderFactory.setXIncludeAware(false);
        documentBuilderFactory.setCoalescing(false);
        documentBuilderFactory.setExpandEntityReferences(false);
        return documentBuilderFactory;
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

    @Override
    protected void updateDatabase(final Document document, final NewsDatabase database) throws Exception {

    }

    private boolean shouldUpdateDatabase(final SyndEntry rssFeedEntry) {
        return !tags.contains(Tag.NEEDS_FILTERING) || hasKeyword(rssFeedEntry.getTitle());
    }

    protected News getNews(final String guid, final SyndFeed rssFeed, final SyndEntry rssFeedEntry) {
        final News news = new News();

        final String titleFromFeed = contentToText(rssFeed.getTitleEx());
        final String title = Strings.isNullOrEmpty(titleFromFeed) ? getTitle() : titleFromFeed;

        news.setDate(rssFeedEntry.getPublishedDate().getTime());
        news.setGuid(guid);
        news.setTitle(title);
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

        if (news.getTags().contains(Tag.ALWAYS_IMPORTANT) || isImportant(title)) {
            news.setImportant(true);
        }

        if (news.getTags().contains(Tag.UNOFFICIAL)) {
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
