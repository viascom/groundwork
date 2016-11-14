package ch.viascom.groundwork.restclient.response.generic;

import lombok.Data;
import org.apache.http.Header;

import java.util.HashMap;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class ResponseHeader {
    private int statusCode;
    private String requestPath;
    private HashMap<String, String> responseHeaders = new HashMap<>();

    public void setResponseHeaders(Header[] responseHeaders) {
        for (Header header : responseHeaders) {
            String name = header.getName();
            String value = header.getValue();
            this.responseHeaders.put(name, value);
        }
    }
}
