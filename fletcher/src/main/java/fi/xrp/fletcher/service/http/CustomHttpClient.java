package fi.xrp.fletcher.service.http;

import com.google.common.net.HttpHeaders;
import fi.xrp.fletcher.utility.UrlUtility;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;
import org.asynchttpclient.Request;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class CustomHttpClient {
    private final @NonNull AsyncHttpClient asyncHttpClient;

    public <T> CompletableFuture<T> executeAsyncHttpGet(final String url, final ResponseMapper<T> mapper) {
        final Request request = Dsl
                .get(url)
                .addHeader(HttpHeaders.USER_AGENT, "xrp.fi")
                .addHeader(HttpHeaders.REFERER, UrlUtility.getBaseUri(url))
                .build();

        return asyncHttpClient
                .executeRequest(request)
                .toCompletableFuture()
                .thenApply(mapper::map);
    }
}
