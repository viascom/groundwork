package ch.viascom.groundwork.foxhttp.authorization;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;

import java.net.URLConnection;

/**
 * FoxHttpAuthorization interface
 *
 * @author patrick.boesch@viascom.ch
 */
@FunctionalInterface
public interface FoxHttpAuthorization {
    void doAuthorization(URLConnection connection, FoxHttpAuthorizationScope foxHttpAuthorizationScope) throws FoxHttpRequestException;
}
