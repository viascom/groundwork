package ch.viascom.groundwork.restclient.http.request.filter.response;

import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.http.request.Request;
import ch.viascom.groundwork.restclient.response.generic.ErrorResponse;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface ResponseExceptionFilter extends ch.viascom.groundwork.restclient.filter.response.ResponseExceptionFilter {

    void filter(Exception error, ErrorResponse errorResponse, Request request) throws RESTClientException;

}
