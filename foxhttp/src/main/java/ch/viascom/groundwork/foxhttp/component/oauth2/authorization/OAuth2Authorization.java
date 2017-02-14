package ch.viascom.groundwork.foxhttp.component.oauth2.authorization;

import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorization;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface OAuth2Authorization extends FoxHttpAuthorization {
    void setValue(String value);
}
