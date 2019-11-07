package fi.xrp.fletcher.service.http;

import com.google.common.net.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.*;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
public class CustomHttpClient {
    private final AsyncHttpClient asyncHttpClient;

    public <T> void executeAsyncHttpGet(final String url, final ResponseMapper<T> mapper, final AsyncResponseHandler<T> handler) {
        final AsyncCompletionHandler<Response> topLevelHandler = new AsyncCompletionHandler<Response>() {
            @Override
            public Response onCompleted(final Response response) throws Exception {
                log.debug("{}: {}", url, response.getStatusCode());

                if (response.hasResponseStatus() && response.hasResponseHeaders() && response.hasResponseBody() && response.getStatusCode() == 200) {
                    handler.onValidResponse(mapper.map(response));
                } else {
                    handler.onInvalidResponse(response);
                }

                return response;
            }
        };

        final Request request = Dsl
                .get(url)
                .addHeader(HttpHeaders.USER_AGENT, "xrp.fi")
                .addHeader(HttpHeaders.REFERER, getReferer(url))
                .build();

        asyncHttpClient.executeRequest(request,
                topLevelHandler);
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
