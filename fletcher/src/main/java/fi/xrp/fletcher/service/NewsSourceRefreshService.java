package fi.xrp.fletcher.service;

import fi.xrp.fletcher.model.api.News;
import fi.xrp.fletcher.model.source.NewsProducer;
import fi.xrp.fletcher.model.source.NewsSourceConfiguration;
import fi.xrp.fletcher.service.aws.CustomMetricsClient;
import fi.xrp.fletcher.service.http.CustomHttpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class NewsSourceRefreshService {
    private final CustomHttpClient customHttpClient;
    private final CustomMetricsClient customMetricsClient;
    private final NewsSourceConfiguration sources;
    private final NewsMerger database;
    private final NewsProducerStatusKeeper status;
    private final int futuresTimeoutSeconds;

    public void startAsyncUpdateAndWait() {
        final Map<NewsProducer, CompletableFuture<List<News>>> newsFutures = new HashMap<>();

        log.info("=== Firing queries for news ===");

        for (final NewsProducer source : sources.getSources()) {
            log.debug("Adding future of {}.", source.getTitle());
            newsFutures.put(source, source.startAsyncUpdate(customHttpClient));
        }

        log.info("=== Waiting for news ===");

        final List<News> result = new ArrayList<>();

        try {
            CompletableFuture
                    .allOf(newsFutures.values().toArray(new CompletableFuture[0]))
                    .get(futuresTimeoutSeconds, TimeUnit.SECONDS);
        } catch (final Exception e) {
            log.warn("Error while waiting for all news.");
        } finally {
            newsFutures.values().stream().forEach(a -> a.cancel(true));
        }

        for (final Map.Entry<NewsProducer, CompletableFuture<List<News>>> entry : newsFutures.entrySet()) {
            try {
                log.info("Waiting for news...");
                final List<News> news = entry.getValue().get();
                log.info("{}: Fetched {} news", entry.getKey().getTitle(), news.size());
                for (final NewsProducer.Tag tag : entry.getKey().getTags()) {
                    customMetricsClient.emitAfterUpdateNewsMetrics(tag.name(), entry.getKey().getId(), 0, news.size());
                }
                result.addAll(news);
            } catch (final Exception e) {
                log.warn("{}: Error while getting news in the given timeout: {}", entry.getKey().getTitle(), e.getMessage());
            }
        }

        log.info("=== Updating news ===");

        database.updateNews(result);
    }
}
