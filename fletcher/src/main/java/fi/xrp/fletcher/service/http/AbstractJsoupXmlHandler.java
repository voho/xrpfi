package fi.xrp.fletcher.service.http;

import org.asynchttpclient.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.nio.charset.StandardCharsets;

public abstract class AbstractJsoupXmlHandler extends AbstractHandler {
    @Override
    public void onSuccess(final Response response) throws Exception {
        final String responseBody = response.getResponseBody(StandardCharsets.UTF_8);
        final Document document = Jsoup.parse(responseBody);
        onSuccess(document);
    }

    public abstract void onSuccess(final Document document) throws Exception;
}
