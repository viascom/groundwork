package ch.viascom.groundwork.foxhttp.ssl;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpSSLTrustStrategyException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpSSLTrustStrategy {
    SSLSocketFactory getSSLSocketFactory(HttpsURLConnection httpsURLConnection) throws FoxHttpSSLTrustStrategyException;
}
