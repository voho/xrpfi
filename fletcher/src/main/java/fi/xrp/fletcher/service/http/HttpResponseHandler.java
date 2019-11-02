package fi.xrp.fletcher.service.http;

import com.google.api.client.http.HttpResponse;

public interface HttpResponseHandler {
    void onSuccess(HttpResponse response) throws Exception;

    void onFailure(HttpResponse response) throws Exception;
}
