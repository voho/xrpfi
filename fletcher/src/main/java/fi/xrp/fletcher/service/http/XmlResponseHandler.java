package fi.xrp.fletcher.service.http;

import lombok.NonNull;
import org.asynchttpclient.Response;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

public class XmlResponseHandler extends ValidatingResponseMapper<Document> {
    private static final ThreadLocal<DocumentBuilder> DOCUMENT_BUILDER = ThreadLocal.withInitial(() -> {
        try {
            return getDocumentBuilder();
        } catch (final Exception e) {
            throw new IllegalStateException("Cannot create document builder.", e);
        }
    });

    private static DocumentBuilder getDocumentBuilder() throws Exception {
        return getDocumentBuilderFactory().newDocumentBuilder();
    }

    private static DocumentBuilderFactory getDocumentBuilderFactory() throws Exception {
        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setIgnoringComments(true);
        documentBuilderFactory.setIgnoringElementContentWhitespace(true);
        documentBuilderFactory.setValidating(false);
        documentBuilderFactory.setXIncludeAware(false);
        documentBuilderFactory.setCoalescing(false);
        documentBuilderFactory.setExpandEntityReferences(false);
        return documentBuilderFactory;
    }

    @Override
    public Document mapValid(final @NonNull Response response) throws Exception {
        final DocumentBuilder documentBuilder = DOCUMENT_BUILDER.get();

        try (final InputStream inputStream = response.getResponseBodyAsStream()) {
            return documentBuilder.parse(inputStream);
        } finally {
            documentBuilder.reset();
        }
    }
}
