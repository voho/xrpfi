package fi.xrp.fletcher.model.source;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import fi.xrp.fletcher.model.api.News;
import fi.xrp.fletcher.service.http.CustomHttpClient;
import fi.xrp.fletcher.service.http.ResponseMapper;
import fi.xrp.fletcher.utility.TextUtility;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

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
    public CompletableFuture<List<News>> startAsyncUpdate(final CustomHttpClient customHttpClient) {
        return customHttpClient
                .executeAsyncHttpGet(getFeedUrl(), getResponseMapper())
                .thenApply(this::extractNews)
                .thenApply(news -> {
                    enrichNews(customHttpClient, news);
                    return news;
                });
    }

    protected void enrichNews(final CustomHttpClient customHttpClient, final List<News> multipleNews) {
        for (final News singleNews : multipleNews) {
            enrichNews(customHttpClient, singleNews);
        }
    }

    protected void enrichNews(final CustomHttpClient customHttpClient, final News news) {
        // NOP
    }

    protected abstract List<News> extractNews(T object);

    protected abstract ResponseMapper<T> getResponseMapper();

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
