package fi.xrp.fletcher.service.http;

import org.asynchttpclient.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JsoupResponseMapper implements ResponseMapper<Document> {
    private final String baseUri;

    public JsoupResponseMapper() {
        this("");
    }

    public JsoupResponseMapper(final String baseUri) {
        this.baseUri = baseUri;
    }

    @Override
    public Document map(final Response response) throws Exception {
        try (final InputStream inputStream = response.getResponseBodyAsStream()) {
            return Jsoup.parse(inputStream, StandardCharsets.UTF_8.name(), baseUri);
        }
    }
}
