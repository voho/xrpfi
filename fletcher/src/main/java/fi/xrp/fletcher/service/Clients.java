package fi.xrp.fletcher.service;

import com.google.common.net.HttpHeaders;
import fi.xrp.fletcher.service.http.AbstractHandler;
import org.asynchttpclient.AsyncHttpClient;

import java.net.URI;

public class Clients {
    private final AsyncHttpClient asyncHttpClient;

    public Clients(final AsyncHttpClient asyncHttpClient) {
        this.asyncHttpClient = asyncHttpClient;
    }

    public void executeAsyncHttpGet(final String url, final int timeoutMs, final AbstractHandler handler) {
        asyncHttpClient
                .prepareGet(url)
                // add this to be safe
                .addHeader(HttpHeaders.USER_AGENT, "xrp.fi")
                .addHeader(HttpHeaders.REFERER, getReferer(url))
                .setReadTimeout(timeoutMs)
                .setRequestTimeout(timeoutMs)
                .execute(handler);
    }

    public void executeAsyncHttpGet(final String url, final AbstractHandler handler) {
        asyncHttpClient
                .prepareGet(url)
                // add this to be safe
                .addHeader(HttpHeaders.USER_AGENT, "xrp.fi")
                .addHeader(HttpHeaders.REFERER, getReferer(url))
                .execute(handler);
    }

    private String getReferer(final String url) {
        try {
            final URI uri = URI.create(url);
            return uri.getScheme() + "://" + uri.getHost();
        } catch (Exception e) {
            return url;
        }
    }
}
