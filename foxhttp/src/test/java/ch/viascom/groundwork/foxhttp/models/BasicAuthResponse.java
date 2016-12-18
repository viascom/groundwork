package ch.viascom.groundwork.foxhttp.models;

import lombok.Data;

import java.io.Serializable;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class BasicAuthResponse implements Serializable {
    private boolean authenticated;
    private String user;
}
