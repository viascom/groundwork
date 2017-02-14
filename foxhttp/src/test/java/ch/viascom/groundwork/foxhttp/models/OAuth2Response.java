package ch.viascom.groundwork.foxhttp.models;

import lombok.Data;

import java.io.Serializable;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class OAuth2Response implements Serializable {
    private long expiresIn;

    private String accessToken;
    private String refreshToken;
}
