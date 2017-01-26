package ch.viascom.groundwork.foxauth.decider.oauth2;

import ch.viascom.groundwork.foxauth.FoxAuth;
import ch.viascom.groundwork.foxauth.decider.FoxAuthDecider;
import ch.viascom.groundwork.foxauth.decider.FoxAuthHttpServletRequestWrapper;
import ch.viascom.groundwork.foxauth.util.OAuth2TypeChecker;
import ch.viascom.groundwork.foxauth.validator.type.AuthValidatorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2PasswordDecider implements FoxAuthDecider {
    private int weight = 50;

    @Override
    public AuthValidatorType getAuthValidatorType() {
        return AuthValidatorType.OAUTH2_PASSWORD;
    }

    @Override
    public boolean isValid(FoxAuthHttpServletRequestWrapper servletRequestWrapper, FoxAuth foxAuth) {
        return OAuth2TypeChecker.hasType(servletRequestWrapper, "password","grant_type");
    }
}
