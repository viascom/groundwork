package ch.viascom.groundwork.restclient.response.generic;

import lombok.Data;

import java.util.HashMap;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class ResponseHeader {
    private int statusCode;
    private String requestPath;
    private HashMap<String, String> responseHeaders = new HashMap<>();
}
