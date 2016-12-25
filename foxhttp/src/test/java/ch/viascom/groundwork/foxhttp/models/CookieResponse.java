package ch.viascom.groundwork.foxhttp.models;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class CookieResponse implements Serializable {
    private HashMap<String,String> cookies;
}
