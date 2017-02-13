package ch.viascom.groundwork.foxhttp.component.oauth2.authorization;

import ch.viascom.groundwork.foxhttp.authorization.BearerTokenAuthorization;

/**
 * @author patrick.boesch@viascom.ch
 */
public class OAuth2BearerTokenAuthorization extends BearerTokenAuthorization implements OAuth2Authorization {

    public OAuth2BearerTokenAuthorization(String token) {
        super(token);
    }

    @Override
    public void setValue(String value) {
        this.setToken(value);
    }
}
