package ch.viascom.groundwork.foxhttp.component.oauth2.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class OAuthTokenErrorResponse implements Serializable {
    /**
     * Error code ({@code "invalid_request"}, {@code "invalid_client"}, {@code "invalid_grant"},
     * {@code "unauthorized_client"}, {@code "unsupported_grant_type"}, {@code "invalid_scope"}, or an
     * extension error code as specified in <a
     * href="http://tools.ietf.org/html/rfc6749#section-8.5">Defining Additional Error Codes</a>).
     */
    private String error;

    /**
     * Human-readable text providing additional information, used to assist the client developer in
     * understanding the error that occurred or {@code null} for none.
     */
    @SerializedName("error_description")
    private String errorDescription;

    /**
     * URI identifying a human-readable web page with information about the error, used to provide the
     * client developer with additional information about the error or {@code null} for none.
     */
    @SerializedName("error_uri")
    private String errorUri;
}
