package fi.xrp.fletcher.service.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.Response;

import java.io.InputStream;

public abstract class AbstractJsonHandler extends AbstractHandler {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void onSuccess(final Response response) throws Exception {
        try (final InputStream inputStream = response.getResponseBodyAsStream()) {
            final JsonNode jsonTree = OBJECT_MAPPER.readTree(inputStream);
            this.onSuccess(jsonTree);
        }
    }

    public abstract void onSuccess(final JsonNode jsonTree);
}
