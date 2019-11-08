package fi.xrp.fletcher.lambda;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.xrp.fletcher.model.api.News;
import fi.xrp.fletcher.model.source.NewsProducer;
import fi.xrp.fletcher.model.source.NewsSourceConfiguration;
import fi.xrp.fletcher.service.NewsMerger;
import fi.xrp.fletcher.service.NewsMergerDefault;
import fi.xrp.fletcher.service.NewsProducerStatusKeeper;
import fi.xrp.fletcher.service.NewsProducerStatusKeeperDefault;
import fi.xrp.fletcher.service.aws.CustomMetricsClient;
import fi.xrp.fletcher.service.aws.CustomS3Client;
import fi.xrp.fletcher.service.http.CustomHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.cookie.ThreadSafeCookieStore;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Handler implements RequestHandler<HandlerRequest, HandlerResponse> {
    private static final String BUCKET = "xrpfi";
    private static final String KEY = "root.json";

    private static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(3);
    private static final Duration DEFAULT_HTTP_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration DEFAULT_FINAL_TIMEOUT = Duration.ofSeconds(40);
    private static final int MAX_REQUEST_RETRY = 1;

    private static final DefaultAsyncHttpClientConfig HTTP_CLIENT_CONFIG = Dsl
            .config()
            .setFollowRedirect(true)
            .setCookieStore(new ThreadSafeCookieStore())
            .setMaxRequestRetry(MAX_REQUEST_RETRY)
            .setConnectTimeout((int) DEFAULT_CONNECT_TIMEOUT.toMillis())
            .setRequestTimeout((int) DEFAULT_HTTP_TIMEOUT.toMillis())
            .setReadTimeout((int) DEFAULT_HTTP_TIMEOUT.toMillis())
            .setMaxRedirects(2)
            .setDisableHttpsEndpointIdentificationAlgorithm(true)
            .setUseInsecureTrustManager(true)
            .setKeepAlive(true)
            .build();

    private final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
    private final AmazonCloudWatch cw = AmazonCloudWatchClientBuilder.defaultClient();

    @Override
    public HandlerResponse handleRequest(final HandlerRequest handlerRequest, final Context context) {
        try {
            try (final AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient(HTTP_CLIENT_CONFIG)) {
                final CustomS3Client customS3Client = new CustomS3Client(s3);
                final CustomMetricsClient customMetricsClient = new CustomMetricsClient(cw);
                final CustomHttpClient customHttpClient = new CustomHttpClient(asyncHttpClient);
                final NewsMerger newsMerger = new NewsMergerDefault(Duration.ofDays(14), 50);
                final NewsProducerStatusKeeper newsProducerStatusKeeper = new NewsProducerStatusKeeperDefault(customMetricsClient);
                final NewsSourceConfiguration newsSourceConfiguration = new NewsSourceConfiguration();

                final Map<NewsProducer, CompletableFuture<List<News>>> futures = new HashMap<>();

                log.info("=== Firing queries for news ===");

                newsProducerStatusKeeper.onGlobalStart();

                for (final NewsProducer source : newsSourceConfiguration.getSources()) {
                    futures.put(source, source.startAsyncUpdate(customHttpClient));
                    newsProducerStatusKeeper.onUpdateStarted(source);
                }

                final Runnable endFuturesRunnable = () -> {
                    log.info("=== Cancelling pending operations ===");
                    futures.values().forEach(n -> n.cancel(false));
                    newsProducerStatusKeeper.onGlobalFinished();
                };

                Executors.newSingleThreadScheduledExecutor().schedule(endFuturesRunnable, DEFAULT_FINAL_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS);

                log.info("=== Waiting for news ===");

                final List<News> result = new ArrayList<>();

                for (final Map.Entry<NewsProducer, CompletableFuture<List<News>>> entry : futures.entrySet()) {
                    try {
                        log.debug("Waiting for news...");
                        final List<News> news = entry.getValue().get();
                        log.info("{}: Fetched {} news", entry.getKey().getTitle(), news.size());
                        newsProducerStatusKeeper.onUpdateFinished(entry.getKey(), news.size());
                        result.addAll(news);
                    } catch (final Exception e) {
                        log.warn("{}: Error while getting news in the given timeout: {}", entry.getKey().getTitle(), e.getMessage());
                        newsProducerStatusKeeper.onUpdateFailed(entry.getKey(), e);
                    }
                }

                log.info("=== Updating news ===");

                newsMerger.updateNews(result);

                final HandlerResponse response = new HandlerResponse(newsMerger.getMergedNews(), newsProducerStatusKeeper.getStatuses(), newsProducerStatusKeeper.getGlobalStatus());
                customS3Client.writeJsonToS3(BUCKET, KEY, response);
                //System.out.println(response.getNews().toString());
                log.info("Writing response: {} news from {} sources: {}: {}", response.getNews().size(), response.getMeta().size(), new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(response));
                return response;
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
