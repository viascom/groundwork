package ch.viascom.groundwork.foxauth.validator;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxAuthValidatorStrategy {

    FoxAuthValidator getFoxAuthValidator(String key);
}
