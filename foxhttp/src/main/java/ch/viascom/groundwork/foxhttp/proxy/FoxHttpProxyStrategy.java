package ch.viascom.groundwork.foxhttp.proxy;

import java.net.Proxy;
import java.net.URL;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpProxyStrategy {
    Proxy getProxy(URL url);

    String getProxyAuthorization(URL url);

    boolean hasProxyAuthorization(URL url);
}
