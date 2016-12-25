package ch.viascom.groundwork.foxhttp.ssl;

import javax.net.ssl.SSLSession;

/**
 * @author patrick.boesch@viascom.ch
 */
public class AllHostTrustStrategy implements FoxHttpHostTrustStrategy {
    @Override
    public boolean verify(String s, SSLSession sslSession) {
        return true;
    }
}
