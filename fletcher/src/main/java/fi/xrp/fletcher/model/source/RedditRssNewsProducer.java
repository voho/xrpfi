package fi.xrp.fletcher.model.source;

import fi.xrp.fletcher.model.api.News;
import fi.xrp.fletcher.service.http.CustomHttpClient;
import lombok.Builder;
import lombok.Singular;

import java.util.Set;

public class RedditRssNewsProducer extends AbstractRssNewsProducer {
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
    protected void enrich(final News news, final CustomHttpClient customHttpClient) {
        super.enrich(news, customHttpClient);
        news.setSourceId("reddit");
    }
}
