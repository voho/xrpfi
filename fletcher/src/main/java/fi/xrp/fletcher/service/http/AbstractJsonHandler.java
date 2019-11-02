package fi.xrp.fletcher.service.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.Response;

import java.nio.charset.StandardCharsets;

public abstract class AbstractJsonHandler extends AbstractHandler {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void onSuccess(final Response response) throws Exception {
        final String responseBody = response.getResponseBody(StandardCharsets.UTF_8);
        final JsonNode jsonTree = OBJECT_MAPPER.readTree(responseBody);
        onSuccess(jsonTree);
    }

    public abstract void onSuccess(final JsonNode jsonTree);
}
