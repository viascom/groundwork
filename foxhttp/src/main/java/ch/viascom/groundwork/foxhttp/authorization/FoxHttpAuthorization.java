package ch.viascom.groundwork.foxhttp.authorization;

import java.net.URLConnection;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpAuthorization {
    void doAuthorization(URLConnection connection, FoxHttpAuthorizationScope foxHttpAuthorizationScope);
}
