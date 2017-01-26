package ch.viascom.groundwork.foxauth.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class DefaultOAuth2Response implements Serializable {
    /**
     * Access token issued by the authorization server.
     */
    @Setter(AccessLevel.PRIVATE)
    private String access_token;

    /**
     * Token type (as specified in <a href="http://tools.ietf.org/html/rfc6749#section-7.1">Access
     * Token Types</a>).
     */
    @Setter(AccessLevel.PRIVATE)
    private String token_type;

    /**
     * Lifetime in seconds of the access token (for example 3600 for an hour) or {@code null} for
     * none.
     */
    @Setter(AccessLevel.PRIVATE)
    private Long expires_in;

    /**
     * Refresh token which can be used to obtain new access tokens.
     */
    @Setter(AccessLevel.PRIVATE)
    private String refresh_token;

    /**
     * Scope of the access token as specified in <a
     * href="http://tools.ietf.org/html/rfc6749#section-3.3">Access Token Scope</a> or {@code null}
     * for none.
     */
    private String scope;

    private String state;

    public void setAccessToken(String accessToken) {
        access_token = accessToken;
    }

    public void setTokenType(String tokenType) {
        token_type = tokenType;
    }

    public void setExpiresInSeconds(Long expiresInSeconds) {
        expires_in = expiresInSeconds;
    }

    public void setRefreshToken(String refreshToken) {
        refresh_token = refreshToken;
    }

}
