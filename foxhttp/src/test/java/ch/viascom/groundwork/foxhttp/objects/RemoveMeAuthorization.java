package ch.viascom.groundwork.foxhttp.objects;

import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorization;
import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationScope;

import java.net.URLConnection;

/**
 * @author patrick.boesch@viascom.ch
 */
public class RemoveMeAuthorization implements FoxHttpAuthorization {
    @Override
    public void doAuthorization(URLConnection connection, FoxHttpAuthorizationScope foxHttpAuthorizationScope) {
        connection.setRequestProperty("Remove-Me", "-----");
    }
}
