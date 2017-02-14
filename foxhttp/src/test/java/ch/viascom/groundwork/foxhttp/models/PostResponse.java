package ch.viascom.groundwork.foxhttp.models;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class PostResponse implements Serializable {
    private String url;
    private String origin;
    private HashMap<String, String> args;
    private HashMap<String, String> headers;


    private HashMap<String, String> form;
    private HashMap<String, String> files;
    private String data;
}
