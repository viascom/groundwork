package ch.viascom.groundwork.foxauth.validator;

import ch.viascom.groundwork.foxauth.FoxAuth;
import ch.viascom.groundwork.foxauth.FoxAuthValidation;
import ch.viascom.groundwork.foxauth.decider.FoxAuthDecider;
import ch.viascom.groundwork.foxauth.decider.FoxAuthDeciderComparator;
import ch.viascom.groundwork.foxauth.decider.FoxAuthHttpServletRequestWrapper;
import ch.viascom.groundwork.foxauth.response.error.ErrorResponsePresets;
import ch.viascom.groundwork.foxauth.validator.type.AuthValidatorType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author patrick.boesch@viascom.ch
 */
public class DefaultAuthValidatorStrategy implements FoxAuthValidatorStrategy {

    private Map<AuthValidatorType, FoxAuthValidator> authValidators = new HashMap<>();

    @Override
    public FoxAuthValidation validate(FoxAuthHttpServletRequestWrapper servletRequestWrapper, FoxAuth foxAuth) {
        System.out.println("-> Call method DefaultAuthValidatorStrategy:validate");
        AuthValidatorType authValidatorType = null;
        FoxAuthValidation foxAuthValidation = new FoxAuthValidation();

        (foxAuth.getFoxAuthDeciders()).sort(new FoxAuthDeciderComparator());

        // Decide Type
        for (FoxAuthDecider foxAuthDecider : foxAuth.getFoxAuthDeciders()) {
            if (foxAuthDecider.isValid(servletRequestWrapper, foxAuth)) {
                authValidatorType = foxAuthDecider.getAuthValidatorType();
                foxAuthValidation.setFoxAuthDecider(foxAuthDecider);
                break;
            }
        }

        if (authValidatorType != null && foxAuth.getFoxAuthValidatorStrategy().getValidator(authValidatorType) != null) {
            // Get FoxAuthValidator
            FoxAuthValidator foxAuthValidator = authValidators.get(authValidatorType);
            foxAuthValidation.setFoxAuthValidator(foxAuthValidator);
            return foxAuthValidator.validate(servletRequestWrapper, foxAuth, foxAuthValidation);
        } else {
            if (foxAuth.isUseOAuth2()) {
                foxAuthValidation.setFoxAuthErrorResponse(ErrorResponsePresets.OAUTH2_UNSUPPORTED_GRANT_TYPE);
            } else {
                foxAuthValidation.setFoxAuthErrorResponse(ErrorResponsePresets.UNSUPPORTED_AUTHORIZATION_TYPE);
            }

            foxAuthValidation.setStatus(false);
            return foxAuthValidation;
        }

    }

    @Override
    public FoxAuthValidator getValidator(AuthValidatorType authValidatorType) {
        return authValidators.get(authValidatorType);
    }

    @Override
    public void addValidator(AuthValidatorType authValidatorType, FoxAuthValidator foxAuthValidator) {
        authValidators.put(authValidatorType, foxAuthValidator);
    }

    @Override
    public void removeValidator(AuthValidatorType authValidatorType) {
        authValidators.remove(authValidatorType);
    }
}
