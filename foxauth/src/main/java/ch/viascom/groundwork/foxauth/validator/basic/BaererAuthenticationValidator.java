package ch.viascom.groundwork.foxauth.validator.basic;

import ch.viascom.groundwork.foxauth.FoxAuth;
import ch.viascom.groundwork.foxauth.FoxAuthValidation;
import ch.viascom.groundwork.foxauth.decider.FoxAuthHttpServletRequestWrapper;
import ch.viascom.groundwork.foxauth.validator.FoxAuthValidator;
import ch.viascom.groundwork.foxauth.validator.type.AuthValidatorType;

/**
 * @author patrick.boesch@viascom.ch
 */
public class BaererAuthenticationValidator implements FoxAuthValidator {
    @Override
    public FoxAuthValidation validate(FoxAuthHttpServletRequestWrapper servletRequestWrapper, FoxAuth foxAuth, FoxAuthValidation foxAuthValidation) {
        String authorization = servletRequestWrapper.getHeaders().get("Authorization");
        String[] authSplit = authorization.split(" ");
        String token = authSplit[1];

        return foxAuth.getFoxAuthDataValidator().validateString(
                token,
                AuthValidatorType.BAERER_AUTHENTICATION,
                foxAuthValidation
        );
    }
}
