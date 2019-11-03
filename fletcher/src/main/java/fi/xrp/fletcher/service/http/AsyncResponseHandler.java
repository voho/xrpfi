package fi.xrp.fletcher.service.http;

import org.asynchttpclient.Response;

public interface AsyncResponseHandler<T> {
    T map(Response response) throws Exception;

    void onValidResponse(Response response, T object) throws Exception;

    void onInvalidResponse(Response response);

    void onThrowable(Throwable throwable);
}
