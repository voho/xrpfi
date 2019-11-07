package fi.xrp.fletcher.service.http;

import org.asynchttpclient.Response;

public interface AsyncResponseHandler<T> {
    void onValidResponse(T object);

    void onInvalidResponse(Response response);
}
