package fi.xrp.fletcher.service;

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
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final AmazonS3 s3;

    public void writeJsonToS3(String bucket, String key, Object object) throws IOException {
        String jsonString = OBJECT_MAPPER.writeValueAsString(object);
        logger.info("Writing JSON of length {} to bucket {} and key {}.", jsonString.length(), bucket, key);

        try (InputStream is = new StringInputStream(jsonString)) {
            ObjectMetadata meta = new ObjectMetadata();
            s3.putObject(bucket, key, is, meta);
        } catch (UnsupportedEncodingException e) {
            throw new IOException(e);
        }
    }
}
