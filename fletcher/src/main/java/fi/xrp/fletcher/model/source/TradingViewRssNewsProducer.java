package fi.xrp.fletcher.model.source;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import fi.xrp.fletcher.model.api.News;
import lombok.Builder;
import lombok.Singular;

import java.util.Locale;
import java.util.Set;

public class TradingViewRssNewsProducer extends AbstractRssNewsProducer {
    private final String sort;
    private final String time;
    private final String symbol;

    @Builder
    public TradingViewRssNewsProducer(final String sort, final String time, final String symbol, @Singular final Set<Tag> tags) {
        super(tags);
        this.sort = sort;
        this.time = time;
        this.symbol = symbol;
    }

    @Override
    public String getTitle() {
        return String.format("Trading View Idea for %s", symbol.toUpperCase(Locale.ROOT));
    }

    @Override
    public String getFeedUrl() {
        return String.format("http://www.tradingview.com/feed/?sort=%s&time=%s&symbol=%s", sort, time, symbol);
    }

    @Override
    protected News getNews(final String guid, final SyndFeed rssFeed, final SyndEntry rssFeedEntry) {
        final News news = super.getNews(guid, rssFeed, rssFeedEntry);
        // TODO more info
        news.setSourceId("trading");
        return news;
    }
}
