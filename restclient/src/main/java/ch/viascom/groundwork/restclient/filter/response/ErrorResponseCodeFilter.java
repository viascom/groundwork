package ch.viascom.groundwork.restclient.filter.response;

import ch.viascom.groundwork.restclient.filter.RESTFilter;
import ch.viascom.groundwork.restclient.response.generic.ErrorResponse;
import ch.viascom.groundwork.restclient.response.generic.Response;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface ErrorResponseCodeFilter extends RESTFilter {

    void filter(int responseCode, Response response, ErrorResponse errorResponse) throws Exception;
}
