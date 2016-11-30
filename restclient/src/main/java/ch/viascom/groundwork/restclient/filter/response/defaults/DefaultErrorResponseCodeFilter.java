package ch.viascom.groundwork.restclient.filter.response.defaults;

import ch.viascom.groundwork.restclient.exception.AccessRESTClientException;
import ch.viascom.groundwork.restclient.exception.AuthorizationRESTClientException;
import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.filter.response.ErrorResponseCodeFilter;
import ch.viascom.groundwork.restclient.response.generic.ErrorResponse;
import ch.viascom.groundwork.restclient.response.generic.Response;

/**
 * @author patrick.boesch@viascom.ch
 */
public class DefaultErrorResponseCodeFilter implements ErrorResponseCodeFilter {
    @Override
    public void filter(int responseCode, Response response, ErrorResponse errorResponse) throws Exception {
        switch (responseCode) {
            case 401:
                throw new AuthorizationRESTClientException(errorResponse, "Response-Statuscode: " + String.valueOf(responseCode));
            case 403:
                throw new AccessRESTClientException(errorResponse, "Response-Statuscode: " + String.valueOf(responseCode));
            default:
                throw new RESTClientException(errorResponse, "Response-Statuscode: " + String.valueOf(responseCode));
        }
    }
}
