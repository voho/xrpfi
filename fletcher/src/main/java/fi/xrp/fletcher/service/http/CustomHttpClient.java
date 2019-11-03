package fi.xrp.fletcher.service.http;

import com.google.common.net.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;

import java.net.URI;
import java.util.concurrent.Future;

@Slf4j
@RequiredArgsConstructor
public class CustomHttpClient {
    private final AsyncHttpClient asyncHttpClient;

    public <T> Future<T> executeAsyncHttpGet(final String url, final AsyncResponseHandler<T> handler) {
        return asyncHttpClient
                .prepareGet(url)
                .addHeader(HttpHeaders.USER_AGENT, "xrp.fi")
                .addHeader(HttpHeaders.REFERER, getReferer(url))
                .execute(new AsyncCompletionHandler<T>() {
                    @Override
                    public T onCompleted(final Response response) throws Exception {
                        log.info("{}: {}", url, response.getStatusCode());
                        try {
                            if (response.hasResponseStatus() && response.hasResponseHeaders() && response.hasResponseBody() && response.getStatusCode() == 200) {
                                final T result = handler.map(response);
                                handler.onValidResponse(response, result);
                                return result;
                            } else {
                                handler.onInvalidResponse(response);
                                throw new IllegalStateException("Invalid response: " + response.getStatusCode() + " for " + url);// TODO
                            }
                        } catch (final Exception e) {
                            handler.onThrowable(e);
                            throw e;
                        }
                    }
                });
    }

    private String getReferer(final String url) {
        try {
            final URI uri = URI.create(url);
            return uri.getScheme() + "://" + uri.getHost();
        } catch (final Exception e) {
            return url;
        }
    }
}
