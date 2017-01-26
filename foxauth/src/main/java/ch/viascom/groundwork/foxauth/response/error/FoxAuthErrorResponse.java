package ch.viascom.groundwork.foxauth.response.error;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxAuthErrorResponse {
    String getError();

    void setError(String error);

    String getError_description();

    void setError_description(String errorDescription);

    String getError_uri();

    void setError_uri(String errorUri);
}
