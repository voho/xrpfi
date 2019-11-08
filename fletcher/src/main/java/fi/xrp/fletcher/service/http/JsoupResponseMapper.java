package fi.xrp.fletcher.service.http;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.asynchttpclient.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class JsoupResponseMapper extends ValidatingResponseMapper<Document> {
    private final @NonNull String baseUri;

    @Override
    protected Document mapValid(final Response response) throws Exception {
        try (final InputStream inputStream = response.getResponseBodyAsStream()) {
            return Jsoup.parse(inputStream, StandardCharsets.UTF_8.name(), baseUri);
        }
    }
}
