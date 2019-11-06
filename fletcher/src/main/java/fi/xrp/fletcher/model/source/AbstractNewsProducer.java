package fi.xrp.fletcher.model.source;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.Futures;
import fi.xrp.fletcher.model.api.News;
import fi.xrp.fletcher.service.NewsMerger;
import fi.xrp.fletcher.service.NewsProducerStatusKeeper;
import fi.xrp.fletcher.service.http.AsyncResponseHandler;
import fi.xrp.fletcher.service.http.CustomHttpClient;
import fi.xrp.fletcher.utility.TextUtility;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.Response;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

@Slf4j
public abstract class AbstractNewsProducer<T> implements NewsProducer {
    private final Set<String> IMPORTANT_MARKERS = Sets.newHashSet(
            "launch", "launches", "launched", "launching", "breaking", "authorized", "authorised", "authorizes", "authorises", "authorize", "authorise"
    );
    private final Set<String> KEYWORDS = Sets.newHashSet(
            "xrp", "ripple", "swell", "xrapid", "xvia", "xcurrent", "swift", "ilp", "dlt"
    );

    protected final Set<Tag> tags;

    @Override
    public Set<Tag> getTags() {
        return tags;
    }

    public AbstractNewsProducer(final Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public Future<List<News>> startAsyncUpdate(final CustomHttpClient customHttpClient, final NewsMerger database, final NewsProducerStatusKeeper status) {
        status.onUpdateStarted(this);

        final Future<T> future = customHttpClient.executeAsyncHttpGet(getFeedUrl(), new AsyncResponseHandler<T>() {
            @Override
            public T map(final Response response) throws Exception {
                return mapResponse(response);
            }

            @Override
            public void onValidResponse(final Response response, final T document) throws Exception {
                status.onUpdateFinished(AbstractNewsProducer.this);
            }

            @Override
            public void onInvalidResponse(final Response response) {
                status.onUpdateFailed(AbstractNewsProducer.this, response);
            }

            @Override
            public void onThrowable(final Throwable throwable) {
                status.onUpdateFailed(AbstractNewsProducer.this, throwable);
            }
        });

        return Futures.lazyTransform(future, a -> {
            final List<News> mapped = mapFuture(a);
            mapped.forEach(b -> enrich(b, customHttpClient));
            return mapped;
        });
    }

    protected abstract void enrich(News news, CustomHttpClient customHttpClient);

    protected abstract List<News> mapFuture(T value);

    protected abstract T mapResponse(Response response) throws Exception;

    @Override
    public String getTitle() {
        final String feedUrl = getHomeUrl();
        if (Strings.isNullOrEmpty(feedUrl)) {
            return "N/A";
        }
        final URI uri = URI.create(feedUrl);
        String name;
        if ("feeds.feedburner.com".equalsIgnoreCase(uri.getHost())) {
            name = uri.getPath();
        } else {
            name = uri.getHost();
        }
        if (Strings.isNullOrEmpty(name)) {
            return feedUrl;
        }
        name = name.replace("www.", "");
        final int lastDotIndex = name.lastIndexOf('.');
        if (lastDotIndex != -1) {
            name = name.substring(0, lastDotIndex);
        }
        while (name.endsWith("/")) {
            name = name.substring(0, name.length() - 1);
        }
        while (name.startsWith("/")) {
            name = name.substring(1);
        }
        return name;
    }

    @Override
    public String getHomeUrl() {
        return getFeedUrl();
    }

    protected boolean hasKeyword(final String title) {
        final Set<String> keywords = TextUtility.getKeywords(title);
        return KEYWORDS.stream().anyMatch(keywords::contains);
    }

    protected boolean isImportant(final String title) {
        final Set<String> keywords = TextUtility.getKeywords(title);
        return IMPORTANT_MARKERS.stream().anyMatch(keywords::contains);
    }
}
