package fi.xrp.fletcher.model.source;

import com.google.common.collect.Sets;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import fi.xrp.fletcher.model.api.News;
import fi.xrp.fletcher.service.http.CustomHttpClient;
import fi.xrp.fletcher.service.http.JsoupResponseMapper;
import lombok.Builder;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;

import java.util.Locale;
import java.util.Set;

@Slf4j
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
        news.setSourceId("trading");
        return news;
    }

    @Override
    protected void enrichNews(final CustomHttpClient customHttpClient, final News news) {
        super.enrichNews(customHttpClient, news);

        customHttpClient
                .executeAsyncHttpGet(news.getUrl(), new JsoupResponseMapper(news.getUrl()))
                .thenAccept(document -> {
                    if (isLong(document)) {
                        news.setTags(Sets.union(news.getTags(), Sets.newHashSet(Tag.BULLISH.name())));
                    }
                    if (isShort(document)) {
                        news.setTags(Sets.union(news.getTags(), Sets.newHashSet(Tag.BEARISH.name())));
                    }
                });
    }

    private boolean isShort(final Document document) {
        return !document.select(".tv-chart-view__header .tv-idea-label--short").isEmpty();
    }

    private boolean isLong(final Document document) {
        return !document.select(".tv-chart-view__header .tv-idea-label--long").isEmpty();
    }
}
