package ch.viascom.groundwork.foxhttp.ssl;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/**
 * @author patrick.boesch@viascom.ch
 */
public class DefaultSSLTrustStrategy implements FoxHttpSSLTrustStrategy {
    @Override
    public SSLSocketFactory getSSLSocketFactory(HttpsURLConnection httpsURLConnection) {
        return HttpsURLConnection.getDefaultSSLSocketFactory();
    }
}
