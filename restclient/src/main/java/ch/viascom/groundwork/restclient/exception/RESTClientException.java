package ch.viascom.groundwork.restclient.exception;

import ch.viascom.groundwork.restclient.response.generic.ErrorResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author patrick.boesch@viascom.ch
 */
public class RESTClientException extends Exception {
    @Getter
    @Setter
    private ErrorResponse errorResponse;

    public RESTClientException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public RESTClientException(ErrorResponse errorResponse, String message) {
        super(message);
        this.errorResponse = errorResponse;
    }
}
