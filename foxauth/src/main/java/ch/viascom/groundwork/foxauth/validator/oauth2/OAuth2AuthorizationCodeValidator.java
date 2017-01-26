package ch.viascom.groundwork.foxauth.validator.oauth2;

import ch.viascom.groundwork.foxauth.FoxAuth;
import ch.viascom.groundwork.foxauth.FoxAuthValidation;
import ch.viascom.groundwork.foxauth.decider.FoxAuthHttpServletRequestWrapper;
import ch.viascom.groundwork.foxauth.response.error.ErrorResponsePresets;
import ch.viascom.groundwork.foxauth.util.OAuth2ScopeLoader;
import ch.viascom.groundwork.foxauth.validator.FoxAuthValidator;
import ch.viascom.groundwork.foxauth.validator.type.AuthValidatorType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author patrick.boesch@viascom.ch
 */
public class OAuth2AuthorizationCodeValidator implements FoxAuthValidator {
    @Override
    public FoxAuthValidation validate(FoxAuthHttpServletRequestWrapper servletRequestWrapper, FoxAuth foxAuth, FoxAuthValidation foxAuthValidation) {
        String accessCode = null;
        if(servletRequestWrapper.getMethod().equals("POST")) {
            String body = servletRequestWrapper.getBody().toString();
            Map<String,String> formData = new HashMap<>();

            String[] formElements = body.split("&");
            for (String element : formElements) {
                String[] data = element.split("=");
                formData.put(data[0],data[1]);
            }

            accessCode = formData.get("code");
            foxAuthValidation.setRedirectURI(formData.get("redirect_uri"));
            foxAuthValidation.setClientId(formData.get("client_id"));
        }else{
            accessCode = servletRequestWrapper.getQuery().get("code");
            foxAuthValidation.setRedirectURI(servletRequestWrapper.getQuery().get("redirect_uri"));
            foxAuthValidation.setClientId(servletRequestWrapper.getQuery().get("client_id"));
        }



        if (accessCode != null) {
            String scopes = OAuth2ScopeLoader.getScopes(servletRequestWrapper);

            return foxAuth.getFoxAuthDataValidator().validateStringScope(
                    accessCode,
                    scopes,
                    AuthValidatorType.OAUTH2_AUTHORIZATION_CODE,
                    foxAuthValidation
            );

        }
        foxAuthValidation.setStatus(false);
        foxAuthValidation.setFoxAuthErrorResponse(ErrorResponsePresets.OAUTH2_INVALID_REQUEST);
        return foxAuthValidation;
    }
}
