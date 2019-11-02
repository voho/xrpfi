package fi.xrp.fletcher.service.http;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class AbstractHandler extends AsyncCompletionHandler<Response> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Response onCompleted(final Response response) throws Exception {
        logger.debug("HTTP {}: {}", response.getStatusCode(), response.getUri());

        try {
            if (isSuccessful(response)) {
                onSuccess(response);
            } else {
                onFailure(response);
            }
        } catch (Throwable e) {
            logger.error("Error: " + response.getUri(), e);
            onThrowable(e);
        }

        return response;
    }

    protected abstract void onSuccess(final Response response) throws Exception;

    protected void onFailure(final Response response) throws Exception {
        throw new IOException("Invalid response code: " + response.getStatusCode());
    }

    private boolean isSuccessful(final Response response) {
        return response.hasResponseStatus() && response.hasResponseBody() && response.getStatusCode() == HttpResponseStatus.OK.code();
    }
}
