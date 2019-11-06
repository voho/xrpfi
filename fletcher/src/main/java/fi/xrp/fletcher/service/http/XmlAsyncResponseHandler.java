package fi.xrp.fletcher.service.http;

import org.asynchttpclient.Response;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

public class XmlAsyncResponseHandler implements ResponseMapper<Document> {
    @Override
    public Document map(final Response response) throws Exception {
        final DocumentBuilder documentBuilder = getDocumentBuilder();

        try (final InputStream inputStream = response.getResponseBodyAsStream()) {
            return documentBuilder.parse(inputStream);
        }
    }

    protected DocumentBuilder getDocumentBuilder() throws Exception {
        return getDocumentBuilderFactory().newDocumentBuilder();
    }

    protected DocumentBuilderFactory getDocumentBuilderFactory() throws Exception {
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setIgnoringComments(true);
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);
        documentBuilderFactory.setValidating(false);
        documentBuilderFactory.setXIncludeAware(false);
        documentBuilderFactory.setCoalescing(false);
        documentBuilderFactory.setExpandEntityReferences(false);
        return documentBuilderFactory;
    }
}
