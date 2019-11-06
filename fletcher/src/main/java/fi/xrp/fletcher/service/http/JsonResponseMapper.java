package fi.xrp.fletcher.service.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.Response;

import java.io.InputStream;

public class JsonResponseMapper implements ResponseMapper<JsonNode> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public JsonNode map(final Response response) throws Exception {
        try (final InputStream inputStream = response.getResponseBodyAsStream()) {
            return OBJECT_MAPPER.readTree(inputStream);
        }
    }
}
