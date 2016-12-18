package ch.viascom.groundwork.foxhttp.util;

import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationScope;

/**
 * @author patrick.boesch@viascom.ch
 */
public class RegexUtil {

    public static boolean doesURLMatch(FoxHttpAuthorizationScope inputScope, String compareScope) {
        if (inputScope.getUrl().equals(compareScope)) {
            return true;
        }
        String matchRegex = compareScope.replaceAll("\\*", "[ -~]*").replaceAll("\\.", "\\\\.").replaceAll("\\/", "\\\\/");
        return inputScope.toString().matches(matchRegex);
    }
}
