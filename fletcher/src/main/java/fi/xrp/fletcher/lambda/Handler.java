package fi.xrp.fletcher.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import fi.xrp.fletcher.service.CustomS3Client;

import java.io.IOException;

public class Handler implements RequestHandler<HandlerRequest, HandlerResponse> {
    private final CustomS3Client customS3Client;

    public Handler() {
        AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
        this.customS3Client = new CustomS3Client(s3);
    }

    @Override
    public HandlerResponse handleRequest(HandlerRequest handlerRequest, Context context) {
        try {
            customS3Client.writeJsonToS3("xrpfi", "test.json", "Hi!");
            return new HandlerResponse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
