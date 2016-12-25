package ch.viascom.groundwork.foxhttp.models;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class GetResponse implements Serializable {
    private String url;
    private String origin;
    private String method;
    private HashMap<String, String> args;
    private HashMap<String, String> headers;
}
