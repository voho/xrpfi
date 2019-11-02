package fi.xrp.fletcher.model.source;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import fi.xrp.fletcher.service.Clients;
import fi.xrp.fletcher.service.http.AbstractJsoupXmlHandler;
import lombok.Builder;
import lombok.Singular;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.Set;

public class TradingViewRssNewsProducer extends AbstractRssNewsProducer {
    private final static int META_TIMEOUT_MS = 10000;

    private final Logger logger = LoggerFactory.getLogger(getClass());

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
    protected void updateDatabase(final Clients clients, final NewsGraph database, final String guid, final SyndFeed rssFeed, final SyndEntry rssFeedEntry) {
        super.updateDatabase(clients, database, guid, rssFeed, rssFeedEntry);

        database.attachTradingViewSource(guid);

        clients.executeAsyncHttpGet(rssFeedEntry.getUri(), META_TIMEOUT_MS, new AbstractJsoupXmlHandler() {
            @Override
            public void onSuccess(final Document document) throws Exception {
                if (isLong(document)) {
                    database.markAsBullish(guid);
                }
                if (isShort(document)) {
                    database.markAsBearish(guid);
                }
            }

            @Override
            public void onThrowable(final Throwable error) {
                logger.error("Error while fetching trading metadata.", error);
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
