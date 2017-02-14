package ch.viascom.groundwork.foxhttp.component.oauth2.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class OAuthTokenResponse implements Serializable {
    /** Access token issued by the authorization server. */
    @SerializedName("access_token")
    private String accessToken;

    /**
     * Token type (as specified in <a href="http://tools.ietf.org/html/rfc6749#section-7.1">Access
     * Token Types</a>).
     */
    @SerializedName("token_type")
    private String tokenType;

    /**
     * Lifetime in seconds of the access token (for example 3600 for an hour) or {@code null} for
     * none.
     */
    @SerializedName("expires_in")
    private Long expiresInSeconds;

    /**
     * Refresh token which can be used to obtain new access tokens.
     */
    @SerializedName("refresh_token")
    private String refreshToken;

    /**
     * Scope of the access token as specified in <a
     * href="http://tools.ietf.org/html/rfc6749#section-3.3">Access Token Scope</a> or {@code null}
     * for none.
     */
    private String scope;
}
