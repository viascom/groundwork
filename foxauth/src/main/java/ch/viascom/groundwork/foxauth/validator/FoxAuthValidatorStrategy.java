package ch.viascom.groundwork.foxauth.validator;

import ch.viascom.groundwork.foxauth.FoxAuth;
import ch.viascom.groundwork.foxauth.FoxAuthValidation;
import ch.viascom.groundwork.foxauth.decider.FoxAuthHttpServletRequestWrapper;
import ch.viascom.groundwork.foxauth.validator.type.AuthValidatorType;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxAuthValidatorStrategy {

    FoxAuthValidation validate(FoxAuthHttpServletRequestWrapper servletRequestWrapper, FoxAuth foxAuth);

    FoxAuthValidator getValidator(AuthValidatorType authValidatorType);

    void addValidator(AuthValidatorType authValidatorType, FoxAuthValidator foxAuthValidator);

    void removeValidator(AuthValidatorType authValidatorType);
}
