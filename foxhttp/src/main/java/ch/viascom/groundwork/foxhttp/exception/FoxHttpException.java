package ch.viascom.groundwork.foxhttp.exception;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpException extends Exception {
    public FoxHttpException(Throwable cause) {
        super(cause);
    }

    public FoxHttpException(String message) {
        super(message);
    }

    public FoxHttpException(String message, Throwable cause) {
        super(message, cause);
    }
}
