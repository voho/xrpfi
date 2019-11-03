package fi.xrp.fletcher.service;

import fi.xrp.fletcher.model.api.News;
import fi.xrp.fletcher.model.source.NewsDatabase;
import fi.xrp.fletcher.model.source.NewsProducer;
import fi.xrp.fletcher.model.source.NewsProducerStatus;
import fi.xrp.fletcher.model.source.NewsSourceConfiguration;
import fi.xrp.fletcher.service.http.CustomHttpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class NewsSourceRefreshService {
    private final CustomHttpClient customHttpClient;
    private final NewsSourceConfiguration sources;
    private final NewsDatabase database;
    private final NewsProducerStatus status;
    private final Duration futuresTimeout;

    public void startAsyncUpdateAndWait() {
        final List<Future<List<News>>> newsFutures = new ArrayList<>();

        for (final NewsProducer source : sources.getSources()) {
            log.info("Adding future of {}.", source.getTitle());
            newsFutures.add(source.startAsyncUpdate(customHttpClient, database, status));
        }

        final List<News> result = new ArrayList<>();

        for (final Future<List<News>> newsFuture : newsFutures) {
            try {
                final List<News> news = newsFuture.get(futuresTimeout.toMillis(), TimeUnit.MILLISECONDS);
                log.info("Obtained {} news.", news.size());
                news.addAll(news);
            } catch (final Exception e) {
                log.warn("Error while getting news in the given timeout.", e);
            }
        }

        database.setNews(result);
    }
}
