package fi.xrp.fletcher.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.Set;

@Slf4j
public class RedditRssNewsProducer extends AbstractNewsProducer {
    private final String sub;

    @Builder
    public RedditRssNewsProducer(final String sub, @Singular final Set<Tag> tags) {
        super(tags);
        this.sub = sub;
    }

    @Override
    public String getFeedUrl() {
        return String.format("http://www.reddit.com/r/%s/.json", this.sub);
    }

    @Override
    public String getHomeUrl() {
        return String.format("http://www.reddit.com/r/%s/", this.sub);
    }

    @Override
    protected HttpResponseHandler getHandler(final NewsListener newsListener) {
        return null;
    }

    @Override
    public String getTitle() {
        return String.format("Reddit: /r/%s", this.sub);
    }

    private String getThumbnailUrl(final JsonNode jsonTree) {
        final String type = jsonTree.at("/0/data/children/0/data/media/type").asText();

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
