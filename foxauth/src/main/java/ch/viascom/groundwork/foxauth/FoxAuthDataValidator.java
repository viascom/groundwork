package ch.viascom.groundwork.foxauth;

import ch.viascom.groundwork.foxauth.decider.FoxAuthHttpServletRequestWrapper;
import ch.viascom.groundwork.foxauth.response.error.FoxAuthErrorResponse;
import ch.viascom.groundwork.foxauth.validator.type.AuthValidatorType;

/**
 * @author patrick.boesch@viascom.ch
 */
public abstract class FoxAuthDataValidator {

    protected void setSuccessful(FoxAuthValidation foxAuthValidation) {
        foxAuthValidation.setStatus(true);
    }

    protected void setFailed(FoxAuthValidation foxAuthValidation, FoxAuthErrorResponse foxAuthErrorResponse) {
        foxAuthValidation.setStatus(false);
        foxAuthValidation.setFoxAuthErrorResponse(foxAuthErrorResponse);
    }

    public abstract FoxAuthValidation validate(FoxAuthHttpServletRequestWrapper servletRequestWrapper, AuthValidatorType authValidatorType, FoxAuthValidation foxAuthValidation);

    public abstract FoxAuthValidation validateUsernamePassword(String username, String password, AuthValidatorType authValidatorType, FoxAuthValidation foxAuthValidation);

    public abstract FoxAuthValidation validateUsernamePasswordScope(String username, String password, String requestedScopes, AuthValidatorType authValidatorType, FoxAuthValidation foxAuthValidation);

    public abstract FoxAuthValidation validateString(String code, AuthValidatorType authValidatorType, FoxAuthValidation foxAuthValidation);

    public abstract FoxAuthValidation validateStringScope(String code, String requestedScopes, AuthValidatorType authValidatorType, FoxAuthValidation foxAuthValidation);

    public abstract FoxAuthValidation validateObject(Object o, AuthValidatorType authValidatorType, FoxAuthValidation foxAuthValidation);

    public abstract FoxAuthValidation validateObjectScope(Object o, String requestedScopes, AuthValidatorType authValidatorType, FoxAuthValidation foxAuthValidation);
}
