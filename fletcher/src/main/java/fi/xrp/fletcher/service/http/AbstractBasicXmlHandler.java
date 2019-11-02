package fi.xrp.fletcher.service.http;

import fi.xrp.fletcher.utility.TextUtility;
import org.asynchttpclient.Response;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public abstract class AbstractBasicXmlHandler extends AbstractHandler {
    private final DocumentBuilderFactory documentBuilderFactory;
    private final DocumentBuilder documentBuilder;

    public AbstractBasicXmlHandler() {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setIgnoringComments(true);
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);
        documentBuilderFactory.setValidating(false);
        documentBuilderFactory.setXIncludeAware(false);
        documentBuilderFactory.setCoalescing(false);
        documentBuilderFactory.setExpandEntityReferences(false);

        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onSuccess(final Response response) throws Exception {
        final String responseBody = TextUtility.xmlCleanup(response.getResponseBody(StandardCharsets.UTF_8));
        final byte[] responseBodyBytes = responseBody.getBytes(StandardCharsets.UTF_8);
        try (final InputStream inputStream = new ByteArrayInputStream(responseBodyBytes)) {
            final Document document = documentBuilder.parse(inputStream);
            onSuccess(document);
        }
    }

    public abstract void onSuccess(Document document) throws Exception;
}
