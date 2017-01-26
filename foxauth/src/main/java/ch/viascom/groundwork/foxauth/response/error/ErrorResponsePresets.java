package ch.viascom.groundwork.foxauth.response.error;

/**
 * @author patrick.boesch@viascom.ch
 */
public class ErrorResponsePresets extends DefaultErrorResponse {

    public static final DefaultErrorResponse SERVER_ERROR = create("server_error","The authorization server encountered an unexpected condition that prevented it from fulfilling the request. (This error code is needed because a 500 Internal Server Error HTTP status code cannot be returned to the client via an HTTP redirect.)");
    public static final DefaultErrorResponse TEMPORARILY_UNAVAILABLE = create("temporarily_unavailable","The authorization server is currently unable to handle the request due to a temporary overloading or maintenance of the server.  (This error code is needed because a 503 Service Unavailable HTTP status code cannot be returned to the client via an HTTP redirect.)");

    //OAuth2
    public static final DefaultErrorResponse OAUTH2_INVALID_REQUEST = create("invalid_request", "The request is missing a required parameter, includes an unsupported parameter value (other than grant type), repeats a parameter, includes multiple credentials, utilizes more than one mechanism for authenticating the client, or is otherwise malformed.");
    public static final DefaultErrorResponse OAUTH2_INVALID_CLIENT = create("invalid_client", "Client authentication failed (e.g., unknown client, no client authentication included, or unsupported authentication method).  The authorization server MAY return an HTTP 401 (Unauthorized) status code to indicate which HTTP authentication schemes are supported.  If the client attempted to authenticate via the \"Authorization\" request header field, the authorization server MUST respond with an HTTP 401 (Unauthorized) status code and include the \"WWW-Authenticate\" response header field matching the authentication scheme used by the client.");
    public static final DefaultErrorResponse OAUTH2_INVALID_GRANT = create("invalid_grant", "The provided authorization grant (e.g., authorization code, resource owner credentials) or refresh token is invalid, expired, revoked, does not match the redirection URI used in the authorization request, or was issued to another client.");
    public static final DefaultErrorResponse OAUTH2_UNAUTHORIZED_CLIENT = create("unauthorized_client", "The authenticated client is not authorized to use this authorization grant type.");
    public static final DefaultErrorResponse OAUTH2_UNSUPPORTED_GRANT_TYPE = create("unsupported_grant_type", "The authorization grant type is not supported by the authorization server.");
    public static final DefaultErrorResponse OAUTH2_UNSUPPORTED_RESPONSE_TYPE = create("unsupported_response_type","The authorization server does not support obtaining an authorization code using this method.");
    public static final DefaultErrorResponse OAUTH2_INVALID_SCOPE = create("invalid_scope", "The requested scope is invalid, unknown, malformed, or exceeds the scope granted by the resource owner.");
    public static final DefaultErrorResponse OAUTH2_ACCESS_DENIED = create("access_denied","The resource owner or authorization server denied the request.");

    //Other
    public static final DefaultErrorResponse UNSUPPORTED_AUTHORIZATION_TYPE = create("unsupported_authorization_type","The authorization type is not supported by the authorization server.");

    ErrorResponsePresets(final String error, final String errorDescription, final String errorUri) {
        setError(error);
        setError_description(errorDescription);
        setError_uri(errorUri);
    }

    public static DefaultErrorResponse create(final String error, final String errorDescription, final String errorUri) {
        if (error == null) {
            throw new IllegalArgumentException("ErrorResponsePresets may not be null");
        }
        String type = error.trim().toLowerCase();
        if (type.length() == 0) {
            throw new IllegalArgumentException("ErrorResponsePresets may not be empty");
        }
        return new ErrorResponsePresets(error, errorDescription, errorUri);
    }

    public static DefaultErrorResponse create(final String error, final String errorDescription) {
        if (error == null) {
            throw new IllegalArgumentException("ErrorResponsePresets may not be null");
        }
        String type = error.trim().toLowerCase();
        if (type.length() == 0) {
            throw new IllegalArgumentException("ErrorResponsePresets may not be empty");
        }
        return new ErrorResponsePresets(error, errorDescription, "");
    }
}
