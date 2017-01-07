package ch.viascom.groundwork.restclient.android.model;

import ch.viascom.groundwork.restclient.response.generic.Response;
import ch.viascom.groundwork.restclient.response.generic.ResponseHeader;
import lombok.Data;

import java.util.HashMap;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class HeaderResponse implements Response {
    private ResponseHeader responseHeader;
    private HashMap<String, String> headers;
}