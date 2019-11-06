package fi.xrp.fletcher.service;

import fi.xrp.fletcher.model.api.News;
import fi.xrp.fletcher.model.api.NewsProducerStatus;
import fi.xrp.fletcher.model.source.NewsProducer;
import fi.xrp.fletcher.model.source.NewsSourceConfiguration;
import fi.xrp.fletcher.service.aws.CustomMetricsClient;
import fi.xrp.fletcher.service.http.CustomHttpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class NewsSourceRefreshService {
    private final CustomHttpClient customHttpClient;
    private final CustomMetricsClient customMetricsClient;
    private final NewsSourceConfiguration sources;
    private final NewsMerger database;
    private final NewsProducerStatusKeeper status;
    private final Duration futuresTimeout;

    public void startAsyncUpdateAndWait() {
        final Map<NewsProducer, Future<List<News>>> newsFutures = new HashMap<>();

        for (final NewsProducer source : sources.getSources()) {
            log.debug("Adding future of {}.", source.getTitle());
            newsFutures.put(source, source.startAsyncUpdate(customHttpClient, database, status));
        }

        final List<News> result = new ArrayList<>();

        for (final Map.Entry<NewsProducer, Future<List<News>>> entry : newsFutures.entrySet()) {
            try {
                final NewsProducerStatus meta = status.getStatus(entry.getKey());
                final List<News> news = entry.getValue().get(futuresTimeout.toMillis(), TimeUnit.MILLISECONDS);
                log.debug("{}: Fetched {} news", entry.getKey().getTitle(), news.size());
                for (final NewsProducer.Tag tag : entry.getKey().getTags()) {
                    customMetricsClient.emitAfterUpdateNewsMetrics(tag.name(), entry.getKey().getId(), Math.max(0, meta.getLastUpdateEndDate() - meta.getLastUpdateStartDate()), news.size());
                }
                result.addAll(news);
            } catch (final Exception e) {
                log.warn("{}: Error while getting news in the given timeout: {}", entry.getKey().getTitle(), e.getMessage());
                e.printStackTrace();
            }
        }

        database.updateNews(result);
    }
}
