package ch.viascom.groundwork.foxauth.decider.basic;

import ch.viascom.groundwork.foxauth.FoxAuth;
import ch.viascom.groundwork.foxauth.decider.FoxAuthDecider;
import ch.viascom.groundwork.foxauth.decider.FoxAuthHttpServletRequestWrapper;
import ch.viascom.groundwork.foxauth.validator.type.AuthValidatorType;
import lombok.*;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicAuthenticationDecider implements FoxAuthDecider {

    private int weight = 50;

    @Override
    public AuthValidatorType getAuthValidatorType() {
        return AuthValidatorType.BASIC_AUTHENTICATION;
    }

    @Override
    public boolean isValid(FoxAuthHttpServletRequestWrapper servletRequestWrapper, FoxAuth foxAuth) {
        String authorization = servletRequestWrapper.getHeaders().get("Authorization");
        if(authorization != null){
            String[] authSplit = authorization.split(" ");
            String auth = authSplit[0];
            return (auth.equals("Basic"));
        }else {
            return false;
        }
    }
}
