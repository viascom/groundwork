package ch.viascom.groundwork.foxauth.decider;

import ch.viascom.groundwork.foxauth.FoxAuth;
import ch.viascom.groundwork.foxauth.validator.type.AuthValidatorType;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxAuthDecider {
    int getWeight();
    AuthValidatorType getAuthValidatorType();
    boolean isValid(FoxAuthHttpServletRequestWrapper servletRequestWrapper, FoxAuth foxAuth);
}
