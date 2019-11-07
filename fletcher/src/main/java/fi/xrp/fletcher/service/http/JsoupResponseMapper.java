package fi.xrp.fletcher.service.http;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.asynchttpclient.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class JsoupResponseMapper implements ResponseMapper<Document> {
    private final @NonNull String baseUri;

    @Override
    public Document map(final Response response) throws Exception {
        try (final InputStream inputStream = response.getResponseBodyAsStream()) {
            return Jsoup.parse(inputStream, StandardCharsets.UTF_8.name(), baseUri);
        }
    }
}
