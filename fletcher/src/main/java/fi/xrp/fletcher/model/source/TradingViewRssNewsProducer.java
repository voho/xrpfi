package fi.xrp.fletcher.model.source;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import fi.xrp.fletcher.model.api.News;
import fi.xrp.fletcher.service.http.AsyncResponseHandler;
import fi.xrp.fletcher.service.http.CustomHttpClient;
import lombok.Builder;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.Response;
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
    protected void enrich(final News news, final CustomHttpClient customHttpClient) {
        news.setSourceId("trading");

        customHttpClient.executeAsyncHttpGet(news.getUrl(), new AsyncResponseHandler<Document>() {
            @Override
            public Document map(final Response response) throws Exception {
                return null;
            }

            @Override
            public void onValidResponse(final Response response, final Document object) throws Exception {
                // NOP
            }

            @Override
            public void onInvalidResponse(final Response response) {
                // NOP
            }

            @Override
            public void onThrowable(final Throwable throwable) {
                // NOP
            }
        });
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
