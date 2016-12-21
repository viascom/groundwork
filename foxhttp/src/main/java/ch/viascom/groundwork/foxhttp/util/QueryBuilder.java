package ch.viascom.groundwork.foxhttp.util;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * QueryBuilder fox FoxHttp
 *
 * @author patrick.boesch@viascom.ch
 */
public class QueryBuilder {

    /**
     * Utility classes, which are a collection of static members, are not meant to be instantiated.
     */
    private QueryBuilder() {
        throw new IllegalAccessError("Utility class");
    }

    /**
     * Return a string of key/value pair's based on a map
     *
     * @param entries
     * @return
     * @throws FoxHttpRequestException
     */
    public static String buildQuery(Map<String, String> entries) throws FoxHttpRequestException {
        return buildQuery(entries, "UTF-8");
    }

    /**
     * Return a string of key/value pair's based on a map
     *
     * @param entries
     * @param encoding
     * @return
     * @throws FoxHttpRequestException
     */
    public static String buildQuery(Map<String, String> entries, String encoding) throws FoxHttpRequestException {
        if (entries.size() > 0) {
            StringBuilder sb = new StringBuilder();
            String dataString;
            try {
                for (Map.Entry<String, String> entry : entries.entrySet()) {
                    sb.append(URLEncoder.encode(entry.getKey(), encoding));
                    sb.append("=");
                    sb.append(URLEncoder.encode(entry.getValue(), encoding));
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
