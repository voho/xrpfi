package fi.xrp.fletcher.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import fi.xrp.fletcher.model.api.HandlerRequest;
import fi.xrp.fletcher.model.api.HandlerResponse;
import fi.xrp.fletcher.model.source.NewsSourceConfiguration;
import fi.xrp.fletcher.service.NewsSourceRefreshService;
import fi.xrp.fletcher.service.NewsStorageService;
import fi.xrp.fletcher.service.http.CustomHttpClient;
import fi.xrp.fletcher.service.s3.CustomS3Client;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.Duration;

@Slf4j
public class Handler implements RequestHandler<HandlerRequest, HandlerResponse> {
    private static final String BUCKET = "xrpfi";
    private static final String KEY = "root.json";

    private final AmazonS3 s3;
    private final CustomS3Client customS3Client;
    private final CustomHttpClient customHttpClient;
    private final NewsStorageService newsStorageService;
    private final NewsSourceConfiguration newsSourceConfiguration;
    private final NewsSourceRefreshService newsSourceRefreshService;

    public Handler() {
        this.s3 = AmazonS3ClientBuilder.defaultClient();
        this.customS3Client = new CustomS3Client(this.s3);
        this.customHttpClient = new CustomHttpClient();
        this.newsStorageService = new NewsStorageService(Duration.ofDays(14), 50);
        this.newsSourceConfiguration = new NewsSourceConfiguration();
        this.newsSourceRefreshService = new NewsSourceRefreshService(this.customHttpClient, this.newsStorageService, this.newsSourceConfiguration);
    }

    @Override
    public HandlerResponse handleRequest(final HandlerRequest handlerRequest, final Context context) {
        log.info("Starting processing of {} news sources.", this.newsSourceConfiguration.getSources().size());

        this.newsSourceRefreshService.startUpdate();

        try {
            this.customS3Client.writeJsonToS3(BUCKET, KEY, "{news:[]}");
            return new HandlerResponse(null);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(final String[] args) {
        new Handler().handleRequest(new HandlerRequest(), null);
    }
}
