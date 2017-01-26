package ch.viascom.groundwork.foxauth.validator.oauth2;

import ch.viascom.groundwork.foxauth.FoxAuth;
import ch.viascom.groundwork.foxauth.FoxAuthValidation;
import ch.viascom.groundwork.foxauth.decider.FoxAuthHttpServletRequestWrapper;
import ch.viascom.groundwork.foxauth.response.error.ErrorResponsePresets;
import ch.viascom.groundwork.foxauth.validator.FoxAuthValidator;
import ch.viascom.groundwork.foxauth.validator.type.AuthValidatorType;

/**
 * @author patrick.boesch@viascom.ch
 */
public class OAuth2AuthorizationCodeFlowValidator implements FoxAuthValidator {
    @Override
    public FoxAuthValidation validate(FoxAuthHttpServletRequestWrapper servletRequestWrapper, FoxAuth foxAuth, FoxAuthValidation foxAuthValidation) {

        foxAuthValidation.setRedirectURI(servletRequestWrapper.getQuery().get("redirect_uri"));
        foxAuthValidation.setState(servletRequestWrapper.getQuery().get("state"));
        foxAuthValidation.setClientId(servletRequestWrapper.getQuery().get("client_id"));

        if (foxAuthValidation.getClientId() != null) {

            return foxAuth.getFoxAuthDataValidator().validate(
                    servletRequestWrapper,
                    AuthValidatorType.OAUTH2_AUTHORIZATION_CODE_FLOW,
                    foxAuthValidation
            );
        } else {
            foxAuthValidation.setStatus(false);
            foxAuthValidation.setFoxAuthErrorResponse(ErrorResponsePresets.OAUTH2_INVALID_REQUEST);
            return foxAuthValidation;
        }
    }
}
