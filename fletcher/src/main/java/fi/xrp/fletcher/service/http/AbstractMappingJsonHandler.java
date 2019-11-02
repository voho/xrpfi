package fi.xrp.fletcher.service.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.asynchttpclient.Response;

import java.nio.charset.StandardCharsets;

public abstract class AbstractMappingJsonHandler<T> extends AbstractHandler {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final TypeReference<T> type;

    public AbstractMappingJsonHandler(TypeReference<T> type) {
        this.type = type;
    }

    @Override
    public void onSuccess(final Response response) throws Exception {
        final String responseBody = response.getResponseBody(StandardCharsets.UTF_8);
        final T mappedJsonObject = OBJECT_MAPPER.readValue(responseBody, type);
        onSuccess(mappedJsonObject);
    }

    public abstract void onSuccess(final T object);
}
