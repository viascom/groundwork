package ch.viascom.groundwork.foxauth.validator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author patrick.boesch@viascom.ch
 */
public class DefaultAuthValidatorStrategy implements FoxAuthValidatorStrategy {
    private Map<String, FoxAuthValidator> authValidators = new HashMap<>();

    @Override
    public FoxAuthValidator getFoxAuthValidator(String key) {
        return authValidators.get(key);
    }
}
