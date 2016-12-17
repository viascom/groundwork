package ch.viascom.groundwork.foxhttp.util;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * QueryBuilder fox FoxHttp
 *
 * @author patrick.boesch@viascom.ch
 */
public class QueryBuilder {

    /**
     * Return a string of key/value pair's based on a map
     *
     * @param entries
     * @return
     * @throws FoxHttpRequestException
     */
    public static String buildQuery(HashMap<String, String> entries) throws FoxHttpRequestException {
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
    public static String buildQuery(HashMap<String, String> entries, String encoding) throws FoxHttpRequestException {
        if (entries.size() > 0) {
            StringBuilder sb = new StringBuilder();
            String dataString;
            try {
                for (String key : entries.keySet()) {
                    sb.append(URLEncoder.encode(key, encoding));
                    sb.append("=");
                    sb.append(URLEncoder.encode(entries.get(key), encoding));
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
