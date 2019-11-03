package fi.xrp.fletcher.lambda;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import fi.xrp.fletcher.model.source.NewsDatabase;
import fi.xrp.fletcher.model.source.NewsProducerStatus;
import fi.xrp.fletcher.model.source.NewsSourceConfiguration;
import fi.xrp.fletcher.service.DefaultNewsProducerStatus;
import fi.xrp.fletcher.service.NewsSourceRefreshService;
import fi.xrp.fletcher.service.NewsStorageService;
import fi.xrp.fletcher.service.aws.CustomMetricsClient;
import fi.xrp.fletcher.service.aws.CustomS3Client;
import fi.xrp.fletcher.service.http.CustomHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.cookie.ThreadSafeCookieStore;

import java.io.IOException;
import java.time.Duration;

@Slf4j
public class Handler implements RequestHandler<HandlerRequest, HandlerResponse> {
    private static final String BUCKET = "xrpfi";
    private static final String KEY = "root.json";

    private static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration DEFAULT_HTTP_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration DEFAULT_FUTURES_TIMEOUT = Duration.ofSeconds(40);

    private static final DefaultAsyncHttpClientConfig HTTP_CLIENT_CONFIG = new DefaultAsyncHttpClientConfig.Builder()
            .setFollowRedirect(true)
            .setCookieStore(new ThreadSafeCookieStore())
            .setMaxRequestRetry(1)
            .setConnectTimeout((int) DEFAULT_CONNECT_TIMEOUT.toMillis())
            .setRequestTimeout((int) DEFAULT_HTTP_TIMEOUT.toMillis())
            .setReadTimeout((int) DEFAULT_HTTP_TIMEOUT.toMillis())
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
                final NewsDatabase newsDatabase = new NewsStorageService(Duration.ofDays(14), 50);
                final NewsProducerStatus newsProducerStatus = new DefaultNewsProducerStatus();
                final NewsSourceConfiguration newsSourceConfiguration = new NewsSourceConfiguration();
                final NewsSourceRefreshService newsSourceRefreshService = new NewsSourceRefreshService(customHttpClient, customMetricsClient, newsSourceConfiguration, newsDatabase, newsProducerStatus, DEFAULT_FUTURES_TIMEOUT);

                newsSourceRefreshService.startAsyncUpdateAndWait();

                final HandlerResponse response = new HandlerResponse(newsDatabase.getMergedNews(), newsProducerStatus.getStatuses());
                customS3Client.writeJsonToS3(BUCKET, KEY, response);
                //System.out.println(response.getNews().toString());
                log.info("Writing response: {} news from {} sources", response.getNews().size(), response.getMeta().size());
                return response;
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(final String[] args) {
        new Handler().handleRequest(new HandlerRequest(), null);
    }
}
