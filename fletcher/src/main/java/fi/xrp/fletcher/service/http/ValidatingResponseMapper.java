package fi.xrp.fletcher.service.http;

import lombok.RequiredArgsConstructor;
import org.asynchttpclient.Response;

@RequiredArgsConstructor
abstract class ValidatingResponseMapper<T> implements ResponseMapper<T> {
    @Override
    public T map(final Response response) {
        if (!response.hasResponseStatus() || !response.hasResponseHeaders() || !response.hasResponseBody()) {
            throw new RuntimeException("Incomplete response received.");
        }
        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Invalid response status: " + response.hasResponseStatus());
        }
        try {
            return mapValid(response);
        } catch (final Exception e) {
            throw new RuntimeException("Error while mapping response.", e);
        }
    }

    protected abstract T mapValid(Response response) throws Exception;
}
