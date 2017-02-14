package ch.viascom.groundwork.foxhttp.util;

/**
 * @author patrick.boesch@viascom.ch
 */
public class RegexUtil {

    /**
     * Utility classes, which are a collection of static members, are not meant to be instantiated.
     */
    private RegexUtil() {
        throw new IllegalAccessError("Utility class");
    }

    public static boolean doesURLMatch(String url, String compareUrl) {
        if (url.equals(compareUrl)) {
            return true;
        }
        String matchRegex = compareUrl.replaceAll("\\*", "[ -~]*").replaceAll("\\.", "\\\\.").replaceAll("\\/", "\\\\/");
        return url.matches(matchRegex);
    }
}
