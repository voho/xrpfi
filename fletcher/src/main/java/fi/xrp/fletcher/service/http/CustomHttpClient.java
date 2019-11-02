package fi.xrp.fletcher.service.http;

import com.google.common.net.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.asynchttpclient.AsyncHttpClient;

import java.net.URI;
import java.time.Duration;

@RequiredArgsConstructor
public class CustomHttpClient {
    private final AsyncHttpClient asyncHttpClient;
    private final Duration timeout;

    public void executeAsyncHttpGet(final String url, final AbstractHandler handler) {
        this.asyncHttpClient
                .prepareGet(url)
                .addHeader(HttpHeaders.USER_AGENT, "xrp.fi")
                .addHeader(HttpHeaders.REFERER, this.getReferer(url))
                .setReadTimeout((int) this.timeout.toMillis())
                .setRequestTimeout((int) this.timeout.toMillis())
                .execute(handler);
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
