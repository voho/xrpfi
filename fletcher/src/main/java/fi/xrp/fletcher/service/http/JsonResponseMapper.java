package fi.xrp.fletcher.service.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.asynchttpclient.Response;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public class JsonResponseMapper extends ValidatingResponseMapper<JsonNode> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public JsonNode mapValid(final Response response) throws IOException {
        try (final InputStream inputStream = response.getResponseBodyAsStream()) {
            return OBJECT_MAPPER.readTree(inputStream);
        }
    }
}
