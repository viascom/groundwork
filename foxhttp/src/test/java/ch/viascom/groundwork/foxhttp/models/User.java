package ch.viascom.groundwork.foxhttp.models;

import lombok.Data;

import java.io.Serializable;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class User implements Serializable {
    private String username = "foxhttp@viascom.ch";
    private String firstname = "Fox";
    private String lastname = "Http";
}
