package ch.viascom.groundwork.foxhttp.exception;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpRequestException extends FoxHttpException {
    public FoxHttpRequestException(Throwable cause) {
        super(cause);
    }

    public FoxHttpRequestException(String message) {
        super(message);
    }

    public FoxHttpRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
