package fi.xrp.fletcher.model.source;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;
import com.google.common.net.UrlEscapers;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import fi.xrp.fletcher.service.http.AbstractJsonHandler;
import fi.xrp.fletcher.service.http.CustomHttpClient;
import fi.xrp.fletcher.utility.TextUtility;
import lombok.Builder;
import lombok.Singular;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class TwitterRssNewsProducer extends AbstractRssNewsProducer {
    private static final int META_TIMEOUT_MS = 10000;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String alias;
    private final String name;
    private final String position;
    private final String company;

    @Builder
    public TwitterRssNewsProducer(final String alias, final String name, final String position, final String company, @Singular final Set<Tag> tags) {
        super(tags);
        this.alias = alias;
        this.name = name;
        this.position = position;
        this.company = company;
    }

    @Override
    public String getFeedUrl() {
        return "http://queryfeed.net/twitter?q=from%3A%40" + this.alias + "&title-type=tweet-text-full&order-by=recent&geocode=&omit-direct=on";
    }

    @Override
    public String getHomeUrl() {
        return String.format("http://twitter.com/%s", this.alias);
    }

    @Override
    public String getTitle() {
        return String.format("Twitter: @%s", this.alias);
    }

    @Override
    protected void updateDatabase(final CustomHttpClient customHttpClient, final NewsGraph database, final String guid, final SyndFeed rssFeed, final SyndEntry rssFeedEntry) {
        super.updateDatabase(customHttpClient, database, guid, rssFeed, rssFeedEntry);

        final String oembedUrl = String.format("https://publish.twitter.com/oembed?format=json&dnt=true&theme=dark&url=%s", UrlEscapers.urlPathSegmentEscaper().escape(rssFeedEntry.getUri()));

        database.attachAvatarUrl(
                guid,
                String.format("http://avatars.io/twitter/%s/small", this.alias),
                String.format("http://avatars.io/twitter/%s/medium", this.alias),
                String.format("http://avatars.io/twitter/%s/large", this.alias)
        );

        database.attachTwitterSource(guid, this.alias, null);

        if (!Strings.isNullOrEmpty(this.name) && !Strings.isNullOrEmpty(this.position) && !Strings.isNullOrEmpty(this.company)) {
            database.attachPerson(guid, this.name, this.position, this.company);
        }

        customHttpClient.executeAsyncHttpGet(oembedUrl, META_TIMEOUT_MS, new AbstractJsonHandler() {
            @Override
            public void onSuccess(final JsonNode jsonTree) {
                final String author = jsonTree.at("/author_name").asText().trim();
                final String body = jsonTree.at("/html").asText().trim();
                database.attachBody(guid, body);

                final String bodyPlainText = TextUtility.htmlCleanupLineBreaksOnly(body);
                if (!Strings.isNullOrEmpty(bodyPlainText)) {
                    database.attachTitle(guid, TextUtility.htmlUnescape(bodyPlainText));
                }
                if (!Strings.isNullOrEmpty(author)) {
                    database.attachTwitterSource(guid, TwitterRssNewsProducer.this.alias, author);
                }
            }

            @Override
            protected void onFailure(final Response response) throws Exception {
                if (response.getStatusCode() == 404) {
                    // do nothing if tweet is not found anymore
                    return;
                }
                super.onFailure(response);
            }

            @Override
            public void onThrowable(final Throwable error) {
                TwitterRssNewsProducer.this.logger.error("Error while fetching OEMBED.", error);
            }
        });
    }
}
