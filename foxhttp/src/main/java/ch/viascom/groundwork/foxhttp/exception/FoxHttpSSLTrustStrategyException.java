package ch.viascom.groundwork.foxhttp.exception;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpSSLTrustStrategyException extends FoxHttpException {
    public FoxHttpSSLTrustStrategyException(Throwable cause) {
        super(cause);
    }

    public FoxHttpSSLTrustStrategyException(String message) {
        super(message);
    }

    public FoxHttpSSLTrustStrategyException(String message, Throwable cause) {
        super(message, cause);
    }
}
