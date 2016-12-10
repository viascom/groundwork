package ch.viascom.groundwork.foxhttp.util;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @author patrick.boesch@viascom.ch
 */
public class QueryBuilder {
    public static String buildQuery(HashMap<String, String> entries) throws FoxHttpRequestException {
        if (entries.size() > 0) {
            StringBuilder sb = new StringBuilder();
            String dataString;
            try {
                for (String key : entries.keySet()) {
                    sb.append(URLEncoder.encode(key, "UTF-8"));
                    sb.append("=");
                    sb.append(URLEncoder.encode(entries.get(key), "UTF-8"));
                    sb.append("&");
                }
                sb.deleteCharAt(sb.lastIndexOf("&"));
                dataString = sb.toString();
            } catch (UnsupportedEncodingException e) {
                throw new FoxHttpRequestException(e);
            }
            return dataString;
        } else {
            return "";
        }
    }
}
