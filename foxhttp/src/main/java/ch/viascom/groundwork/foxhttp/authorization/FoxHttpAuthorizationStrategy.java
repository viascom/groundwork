package ch.viascom.groundwork.foxhttp.authorization;

import java.net.URLConnection;
import java.util.ArrayList;

/**
 * FoxHttpAuthorizationStrategy interface
 *
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpAuthorizationStrategy {
    ArrayList<FoxHttpAuthorization> getAuthorization(URLConnection connection, FoxHttpAuthorizationScope foxHttpAuthorizationScope);

    void addAuthorization(FoxHttpAuthorizationScope foxHttpAuthorizationScope, FoxHttpAuthorization foxHttpAuthorization);

    void removeAuthorization(FoxHttpAuthorizationScope foxHttpAuthorizationScope, Class<FoxHttpAuthorization> foxHttpAuthorizationClass);
}