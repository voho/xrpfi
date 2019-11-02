package fi.xrp.fletcher.model.exception;

public class HttpStatusException extends Exception {
    public HttpStatusException(final int status, final String message) {
        super(String.format("%d: %s", status, message));
    }
}
