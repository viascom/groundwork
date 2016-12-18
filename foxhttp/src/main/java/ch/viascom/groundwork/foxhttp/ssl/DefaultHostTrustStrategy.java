package ch.viascom.groundwork.foxhttp.ssl;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * @author patrick.boesch@viascom.ch
 */
public class DefaultHostTrustStrategy implements FoxHttpHostTrustStrategy {

    @Override
    public boolean verify(String s, SSLSession sslSession) {
        return HttpsURLConnection.getDefaultHostnameVerifier().verify(s, sslSession);
    }

}
