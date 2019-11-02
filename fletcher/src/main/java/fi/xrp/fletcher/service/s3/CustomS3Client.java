package fi.xrp.fletcher.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.util.StringInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
public class CustomS3Client {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final AmazonS3 s3;

    public void writeJsonToS3(final String bucket, final String key, final Object object) throws IOException {
        final String jsonString = OBJECT_MAPPER.writeValueAsString(object);
        this.logger.info("Writing JSON of length {} to bucket {} and key {}.", jsonString.length(), bucket, key);

        try (final InputStream is = new StringInputStream(jsonString)) {
            final ObjectMetadata meta = new ObjectMetadata();
            this.s3.putObject(bucket, key, is, meta);
        } catch (final UnsupportedEncodingException e) {
            throw new IOException(e);
        }
    }
}
