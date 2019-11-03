package fi.xrp.fletcher.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.StringInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

@Slf4j
@RequiredArgsConstructor
public class CustomS3Client {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final AmazonS3 s3;

    public void writeJsonToS3(final String bucket, final String key, final Object object) throws IOException {
        final String jsonString = OBJECT_MAPPER.writeValueAsString(object);

        try (final InputStream inputStream = new StringInputStream(jsonString)) {
            final ObjectMetadata meta = new ObjectMetadata();
            final PutObjectRequest request = new PutObjectRequest(bucket, key, inputStream, meta);
            request.setCannedAcl(CannedAccessControlList.PublicRead);
            s3.putObject(request);
        } catch (final UnsupportedEncodingException e) {
            throw new IOException(e);
        }
    }
}
