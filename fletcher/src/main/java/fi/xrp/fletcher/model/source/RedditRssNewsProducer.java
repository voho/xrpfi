package fi.xrp.fletcher.model.source;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import fi.xrp.fletcher.model.api.News;
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
        return String.format("https://www.reddit.com/r/%s/.rss", sub);
    }

    @Override
    public String getHomeUrl() {
        return String.format("https://www.reddit.com/r/%s/", sub);
    }

    @Override
    public String getTitle() {
        return String.format("Reddit: /r/%s", sub);
    }

    @Override
    protected News getNews(final String guid, final SyndFeed rssFeed, final SyndEntry rssFeedEntry) {
        final News news = super.getNews(guid, rssFeed, rssFeedEntry);
        news.setSourceId("reddit");
        return news;
    }
}
