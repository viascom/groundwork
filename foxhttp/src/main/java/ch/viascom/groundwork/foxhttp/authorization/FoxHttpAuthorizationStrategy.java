package ch.viascom.groundwork.foxhttp.authorization;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;

import java.net.URLConnection;
import java.util.List;

/**
 * FoxHttpAuthorizationStrategy interface
 *
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpAuthorizationStrategy {
    List<FoxHttpAuthorization> getAuthorization(URLConnection connection, FoxHttpAuthorizationScope foxHttpAuthorizationScope, FoxHttpClient foxHttpClient);

    void addAuthorization(FoxHttpAuthorizationScope foxHttpAuthorizationScope, FoxHttpAuthorization foxHttpAuthorization);

    void removeAuthorization(FoxHttpAuthorizationScope foxHttpAuthorizationScope, FoxHttpAuthorization foxHttpAuthorization);
}
