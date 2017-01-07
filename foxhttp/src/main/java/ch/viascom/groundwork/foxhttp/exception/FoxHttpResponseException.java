package ch.viascom.groundwork.foxhttp.exception;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpResponseException extends FoxHttpException {
    public FoxHttpResponseException(Throwable cause) {
        super(cause);
    }

    public FoxHttpResponseException(String message) {
        super(message);
    }

    public FoxHttpResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}
