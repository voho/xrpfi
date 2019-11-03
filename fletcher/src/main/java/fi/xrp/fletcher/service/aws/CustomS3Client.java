package fi.xrp.fletcher.service.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomS3Client {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final AmazonS3 s3;

    public void writeJsonToS3(final String bucket, final String key, final Object object) throws IOException {
        final byte[] jsonBytes = OBJECT_MAPPER.writeValueAsBytes(object);
        final ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType("application/json; charset=utf-8");
        meta.setContentLength(jsonBytes.length);
        final PutObjectRequest request = new PutObjectRequest(bucket, key, new ByteArrayInputStream(jsonBytes), meta);
        request.setCannedAcl(CannedAccessControlList.PublicRead);
        s3.putObject(request);
    }
}
