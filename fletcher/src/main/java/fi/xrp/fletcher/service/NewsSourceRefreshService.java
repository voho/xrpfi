package fi.xrp.fletcher.service;

import fi.xrp.fletcher.model.source.NewsListener;
import fi.xrp.fletcher.model.source.NewsProducer;
import fi.xrp.fletcher.model.source.NewsSourceConfiguration;
import fi.xrp.fletcher.service.http.CustomHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NewsSourceRefreshService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CustomHttpClient customHttpClient;
    private final NewsListener newsListener;
    private final List<NewsProducer> sources;

    public NewsSourceRefreshService(final CustomHttpClient customHttpClient, final NewsSourceConfiguration newsSourceConfiguration, final NewsListener newsListener, final NewsGraph graph) {
        this.customHttpClient = customHttpClient;
        this.newsListener = newsListener;
        this.sources = newsSourceConfiguration.getSources();
        for (final NewsProducer source : this.sources) {
            source.scheduleAsyncUpdate(this.customHttpClient, this.newsListener);
        }
        this.logger.info("Number of sources: {}", this.sources.size());
        this.sources.forEach(source -> this.logger.info("Source: {}", source.getTitle()));
    }
}
