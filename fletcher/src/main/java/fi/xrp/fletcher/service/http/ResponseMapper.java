package fi.xrp.fletcher.service.http;

import org.asynchttpclient.Response;

public interface ResponseMapper<T> {
    T map(Response response) throws Exception;
}
