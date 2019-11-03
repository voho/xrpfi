package fi.xrp.fletcher.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.xrp.fletcher.model.api.NewsSourceMeta;
import fi.xrp.fletcher.model.source.NewsDatabase;
import fi.xrp.fletcher.model.source.NewsProducerStatus;
import fi.xrp.fletcher.model.source.NewsSourceConfiguration;
import fi.xrp.fletcher.service.DefaultNewsProducerStatus;
import fi.xrp.fletcher.service.NewsSourceRefreshService;
import fi.xrp.fletcher.service.NewsStorageService;
import fi.xrp.fletcher.service.http.CustomHttpClient;
import fi.xrp.fletcher.service.s3.CustomS3Client;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClientConfig;
import org.asynchttpclient.cookie.ThreadSafeCookieStore;

import java.io.IOException;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class Handler implements RequestHandler<HandlerRequest, HandlerResponse> {
    private static final String BUCKET = "xrpfi";
    private static final String KEY = "root.json";
    private static final Duration DEFAULT_CONNECT_TIMEOUT = Duration.ofSeconds(5);
    private static final Duration DEFAULT_HTTP_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration DEFAULT_FUTURES_TIMEOUT = Duration.ofSeconds(40);

    private final AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient(new DefaultAsyncHttpClientConfig.Builder()
            .setFollowRedirect(true)
            .setCookieStore(new ThreadSafeCookieStore())
            .setMaxRequestRetry(1)
            .setConnectTimeout((int) DEFAULT_CONNECT_TIMEOUT.toMillis())
            .setRequestTimeout((int) DEFAULT_HTTP_TIMEOUT.toMillis())
            .setReadTimeout((int) DEFAULT_HTTP_TIMEOUT.toMillis())
            .build());
    private final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
    private final CustomS3Client customS3Client = new CustomS3Client(s3);
    private final CustomHttpClient customHttpClient = new CustomHttpClient(asyncHttpClient);
    private final NewsDatabase newsDatabase = new NewsStorageService();
    private final NewsProducerStatus newsProducerStatus = new DefaultNewsProducerStatus();
    private final NewsSourceConfiguration newsSourceConfiguration = new NewsSourceConfiguration();
    private final NewsSourceRefreshService newsSourceRefreshService = new NewsSourceRefreshService(customHttpClient, newsSourceConfiguration, newsDatabase, newsProducerStatus, DEFAULT_FUTURES_TIMEOUT);

    @Override
    public HandlerResponse handleRequest(final HandlerRequest handlerRequest, final Context context) {
        final List<NewsSourceMeta> statuses = new LinkedList<>();
        newsSourceRefreshService.startAsyncUpdateAndWait();

        try {
            final HandlerResponse response = new HandlerResponse(newsDatabase.getMergedNews(), statuses);
            final String responseJson = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(response);
            //System.out.println(responseJson);
            customS3Client.writeJsonToS3(BUCKET, KEY, responseJson);
            log.info("Writing response: {} news from {} sources", response.getNews().size(), response.getMeta().size());
            return response;
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                asyncHttpClient.close();
            } catch (final IOException e) {
                // ignore
            }
        }
    }

    public static void main(final String[] args) {
        new Handler().handleRequest(new HandlerRequest(), null);
    }
}
