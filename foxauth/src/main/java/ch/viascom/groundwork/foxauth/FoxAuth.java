package ch.viascom.groundwork.foxauth;

import ch.viascom.groundwork.foxauth.decider.FoxAuthDecider;
import ch.viascom.groundwork.foxauth.decider.FoxAuthHttpServletRequestWrapper;
import ch.viascom.groundwork.foxauth.decider.oauth2.*;
import ch.viascom.groundwork.foxauth.response.error.DefaultErrorResponse;
import ch.viascom.groundwork.foxauth.response.error.FoxAuthErrorResponse;
import ch.viascom.groundwork.foxauth.validator.DefaultAuthValidatorStrategy;
import ch.viascom.groundwork.foxauth.validator.FoxAuthValidator;
import ch.viascom.groundwork.foxauth.validator.FoxAuthValidatorStrategy;
import ch.viascom.groundwork.foxauth.validator.oauth2.OAuth2AuthorizationCodeFlowValidator;
import ch.viascom.groundwork.foxauth.validator.oauth2.OAuth2AuthorizationCodeValidator;
import ch.viascom.groundwork.foxauth.validator.oauth2.OAuth2ClientCredentialValidator;
import lombok.Data;

import javax.enterprise.inject.Alternative;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
@Alternative
public class FoxAuth {

    private List<FoxAuthDecider> foxAuthDeciders = new ArrayList<>();
    private List<FoxAuthDecider> foxAuthorizationEndpointDeciders = new ArrayList<>();
    private FoxAuthDataValidator foxAuthDataValidator;
    private FoxAuthValidatorStrategy foxAuthValidatorStrategy = new DefaultAuthValidatorStrategy();

    private FoxAuthErrorResponse foxAuthErrorResponse = new DefaultErrorResponse();

    private boolean useOAuth2 = false;
    private boolean autoResponseOnError = true;

    public static FoxAuthValidation getFoxAuthValidationFromInterceptor(HttpServletRequest httpServletRequest) {
        return (FoxAuthValidation) httpServletRequest.getAttribute("ch.viascom.groundwork.foxauth.foxAuthValidation");
    }

    public FoxAuthValidation validate(HttpServletRequest httpServletRequest) {
        System.out.println("-> Call method FoxAuth:validate ("+httpServletRequest.getRequestURI()+")");
        if (foxAuthDataValidator == null) {
            throw new IllegalArgumentException("FoxAuthDataValidator is not set!");
        }
        try {
            FoxAuthValidation foxAuthValidation = foxAuthValidatorStrategy.validate(new FoxAuthHttpServletRequestWrapper(httpServletRequest), this);
            return foxAuthValidation;
        } catch (Exception e) {
            e.printStackTrace();
            return new FoxAuthValidation(false, null, "", "", "", null, null, e.getMessage(), "", "");
        }
    }

    public void addFoxAuthValidatorAndDecider(FoxAuthDecider foxAuthDecider, FoxAuthValidator foxAuthValidator) {
        foxAuthDeciders.add(foxAuthDecider);
        foxAuthValidatorStrategy.addValidator(foxAuthDecider.getAuthValidatorType(), foxAuthValidator);
    }

    public void enableOAuth2(int weight) {
        useOAuth2 = true;
        addFoxAuthValidatorAndDecider(new OAuth2ClientCredentialDecider(weight), new OAuth2ClientCredentialValidator());
        addFoxAuthValidatorAndDecider(new OAuth2AuthorizationCodeFlowDecider(weight), new OAuth2AuthorizationCodeFlowValidator());
        addFoxAuthValidatorAndDecider(new OAuth2AuthorizationCodeDecider(weight), new OAuth2AuthorizationCodeValidator());
    }

    public void enableOAuth2() {
        enableOAuth2(50);
    }
}
