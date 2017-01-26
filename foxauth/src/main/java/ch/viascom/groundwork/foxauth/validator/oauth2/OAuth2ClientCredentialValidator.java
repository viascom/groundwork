package ch.viascom.groundwork.foxauth.validator.oauth2;

import ch.viascom.groundwork.foxauth.FoxAuth;
import ch.viascom.groundwork.foxauth.FoxAuthValidation;
import ch.viascom.groundwork.foxauth.decider.FoxAuthHttpServletRequestWrapper;
import ch.viascom.groundwork.foxauth.response.error.ErrorResponsePresets;
import ch.viascom.groundwork.foxauth.util.OAuth2ScopeLoader;
import ch.viascom.groundwork.foxauth.validator.FoxAuthValidator;
import ch.viascom.groundwork.foxauth.validator.type.AuthValidatorType;

import java.util.Base64;

/**
 * @author patrick.boesch@viascom.ch
 */
public class OAuth2ClientCredentialValidator implements FoxAuthValidator {

    @Override
    public FoxAuthValidation validate(FoxAuthHttpServletRequestWrapper servletRequestWrapper, FoxAuth foxAuth, FoxAuthValidation foxAuthValidation) {
        System.out.println("-> Call method OAuth2ClientCredentialValidator:validate");
        String authorization = servletRequestWrapper.getHeaders().get("Authorization");

        if (authorization != null) {
            String[] authSplit = authorization.split(" ");
            if (authSplit.length > 1) {
                String auth = authSplit[0];

                String[] usernamePassword = new String(Base64.getDecoder().decode(authSplit[1])).split(":");

                String scopes = OAuth2ScopeLoader.getScopes(servletRequestWrapper);

                return foxAuth.getFoxAuthDataValidator().validateUsernamePasswordScope(
                        usernamePassword[0],
                        usernamePassword[1],
                        scopes,
                        AuthValidatorType.OAUTH2_CLIENT_CREDENTIALS,
                        foxAuthValidation
                );
            }
        }
        foxAuthValidation.setStatus(false);
        foxAuthValidation.setFoxAuthErrorResponse(ErrorResponsePresets.OAUTH2_INVALID_REQUEST);
        return foxAuthValidation;
    }
}
