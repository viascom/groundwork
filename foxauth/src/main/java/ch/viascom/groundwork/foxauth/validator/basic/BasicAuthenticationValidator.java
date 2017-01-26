package ch.viascom.groundwork.foxauth.validator.basic;

import ch.viascom.groundwork.foxauth.FoxAuth;
import ch.viascom.groundwork.foxauth.FoxAuthValidation;
import ch.viascom.groundwork.foxauth.decider.FoxAuthHttpServletRequestWrapper;
import ch.viascom.groundwork.foxauth.validator.FoxAuthValidator;
import ch.viascom.groundwork.foxauth.validator.type.AuthValidatorType;

import java.util.Base64;

/**
 * @author patrick.boesch@viascom.ch
 */
public class BasicAuthenticationValidator implements FoxAuthValidator {
    @Override
    public FoxAuthValidation validate(FoxAuthHttpServletRequestWrapper servletRequestWrapper, FoxAuth foxAuth, FoxAuthValidation foxAuthValidation) {
        String authorization = servletRequestWrapper.getHeaders().get("Authorization");
        String[] authSplit = authorization.split(" ");
        String[] usernamePassword = new String(Base64.getDecoder().decode(authSplit[1])).split(":");

        return foxAuth.getFoxAuthDataValidator().validateUsernamePassword(
                usernamePassword[0],
                usernamePassword[1],
                AuthValidatorType.BASIC_AUTHENTICATION,
                foxAuthValidation
        );

    }
}
