package ch.viascom.groundwork.restclient.exception;

import ch.viascom.groundwork.restclient.response.generic.ErrorResponse;

/**
 * @author patrick.boesch@viascom.ch
 */
public class AuthorizationRESTClientException extends RESTClientException {
    public AuthorizationRESTClientException(ErrorResponse errorResponse) {
        super(errorResponse);
    }

    public AuthorizationRESTClientException(ErrorResponse errorResponse, String message) {
        super(errorResponse, message);
    }
}
