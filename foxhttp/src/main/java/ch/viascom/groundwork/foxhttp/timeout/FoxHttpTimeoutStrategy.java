package ch.viascom.groundwork.foxhttp.timeout;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpTimeoutStrategy {
    int getConnectionTimeout();
    int getReadTimeout();
}
