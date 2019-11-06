package fi.xrp.fletcher.lambda;

import org.junit.jupiter.api.Test;

class HandlerTest {
    @Test
    public void endToEndTest() {
        new Handler().handleRequest(new HandlerRequest(), null);
    }
}
