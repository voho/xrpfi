package fi.xrp.fletcher.model.source;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;
import com.google.common.html.HtmlEscapers;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import fi.xrp.fletcher.service.CustomHttpClient;
import fi.xrp.fletcher.service.http.AbstractJsonHandler;
import lombok.Builder;
import lombok.Singular;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Set;

public class RedditRssNewsProducer extends AbstractRssNewsProducer {
    private static final int META_TIMEOUT_MS = 10000;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String sub;

    @Builder
    public RedditRssNewsProducer(final String sub, @Singular final Set<Tag> tags) {
        super(tags);
        this.sub = sub;
    }

    @Override
    public String getFeedUrl() {
        return String.format("http://www.reddit.com/r/%s/.rss", sub);
    }

    @Override
    public String getHomeUrl() {
        return String.format("http://www.reddit.com/r/%s/", sub);
    }

    @Override
    public String getTitle() {
        return String.format("Reddit: /r/%s", sub);
    }

    @Override
    protected void updateDatabase(final CustomHttpClient customHttpClient, final NewsGraph database, final String guid, final SyndFeed rssFeed, final SyndEntry rssFeedEntry) {
        super.updateDatabase(customHttpClient, database, guid, rssFeed, rssFeedEntry);

        database.attachRedditSource(guid, sub);

        customHttpClient.executeAsyncHttpGet(rssFeedEntry.getLink() + ".json", META_TIMEOUT_MS, new AbstractJsonHandler() {
            @Override
            public void onSuccess(final JsonNode jsonTree) {
                final long upvotes = getUpVotes(jsonTree);
                final double upvoteRatio = getUpVoteRatio(jsonTree);
                database.attachRating(guid, upvoteRatio, 1.0, upvotes);

                final String thumbnailUrl = getThumbnailUrl(jsonTree);
                if (!Strings.isNullOrEmpty(thumbnailUrl)) {
                    database.attachBody(guid, String.format(
                            "<a href='%s'><img src='%s' alt='' /></a>",
                            rssFeedEntry.getUri(),
                            HtmlEscapers.htmlEscaper().escape(thumbnailUrl)));
                }
            }

            @Override
            public void onFailure(final Response response) throws Exception {
                logger.error("Error while fetching reddit metadata.");
            }

            @Override
            public void onThrowable(final Throwable error) {
                logger.error("Error while fetching reddit metadata.", error);
            }
        });
    }

    private String getThumbnailUrl(JsonNode jsonTree) {
        String type = jsonTree.at("/0/data/children/0/data/media/type").asText();

        switch (type.toLowerCase(Locale.ROOT)) {
            case "imgur.com":
                String url = jsonTree.at("/0/data/children/0/data/media/oembed/thumbnail_url").asText();
                if (url.endsWith("?fb")) {
                    url = url.substring(0, url.length() - 3);
                }
                return url;
            default:
                return null;
        }
    }

    private long getUpVotes(final JsonNode jsonTree) {
        return jsonTree.at("/0/data/children/0/data/ups").asLong();
    }

    private double getUpVoteRatio(final JsonNode jsonTree) {
        return jsonTree.at("/0/data/children/0/data/upvote_ratio").asDouble();
    }
}
