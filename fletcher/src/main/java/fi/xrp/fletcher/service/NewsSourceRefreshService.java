package fi.xrp.fletcher.service;

import fi.xrp.fletcher.model.source.NewsGraph;
import fi.xrp.fletcher.model.source.NewsListener;
import fi.xrp.fletcher.model.source.NewsProducer;
import fi.xrp.fletcher.model.source.NewsSourceConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NewsSourceRefreshService {
    private static final long REFRESH_INTERVAL_SECONDS = 60;
    private static final int NUMBER_OF_THREADS = 8;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CustomHttpClient customHttpClient;
    private final NewsListener newsListener;
    private final NewsGraph graph;
    private final ScheduledExecutorService executorService;
    private final List<NewsProducer> sources;

    public NewsSourceRefreshService(final CustomHttpClient customHttpClient, final NewsSourceConfiguration newsSourceConfiguration, final NewsListener newsListener, final NewsGraph graph) {
        this.customHttpClient = customHttpClient;
        this.newsListener = newsListener;
        this.graph = graph;
        this.sources = newsSourceConfiguration.getSources();
        this.executorService = Executors.newScheduledThreadPool(NUMBER_OF_THREADS);
        this.executorService.scheduleAtFixedRate(this::scheduleAsyncUpdate, 0, REFRESH_INTERVAL_SECONDS, TimeUnit.SECONDS);
        this.logger.info("Number of sources: {}", sources.size());
        sources.forEach(source -> logger.info("Source: {}", source.getTitle()));
    }

    private void scheduleAsyncUpdate() {
        for (final NewsProducer source : sources) {
            source.scheduleAsyncUpdate(customHttpClient, newsListener, graph);
        }
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
    }
}
