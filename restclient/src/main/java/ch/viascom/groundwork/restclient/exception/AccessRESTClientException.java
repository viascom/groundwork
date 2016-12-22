package ch.viascom.groundwork.restclient.exception;

import ch.viascom.groundwork.restclient.response.generic.ErrorResponse;

/**
 * @author patrick.boesch@viascom.ch
 */
public class AccessRESTClientException extends RESTClientException {

    public AccessRESTClientException(ErrorResponse errorResponse) {
        super(errorResponse);
    }

    public AccessRESTClientException(ErrorResponse errorResponse, String message) {
        super(errorResponse, message);
    }
}
