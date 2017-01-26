package ch.viascom.groundwork.foxauth.util;

import ch.viascom.groundwork.foxauth.decider.FoxAuthHttpServletRequestWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author patrick.boesch@viascom.ch
 */
public class OAuth2TypeChecker {

    public static boolean hasType(FoxAuthHttpServletRequestWrapper servletRequestWrapper, String checkType, String typeName) {
        try {
            String type = servletRequestWrapper.getQuery().get(typeName);
            if (servletRequestWrapper.getHttpServletRequest().getMethod().equals("POST")) {
                String body = servletRequestWrapper.getBody().toString();

                Pattern pattern = Pattern.compile(typeName + "=([a-zA-Z_]*)", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(body);

                List<String> listMatches = new ArrayList<>();

                while (matcher.find()) {
                    listMatches.add(matcher.group(1));
                }
                if (listMatches.size() > 0) {
                    type = listMatches.get(0);
                }
            }

            return (type != null && type.equals(checkType));
        } catch (Exception e) {
            return false;
        }
    }

}
