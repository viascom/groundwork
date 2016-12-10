package ch.viascom.groundwork.restclient.filter.response;

import ch.viascom.groundwork.restclient.filter.RESTFilter;
import ch.viascom.groundwork.restclient.response.generic.Response;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface SuccessResponseCodeFilter extends RESTFilter {

    void filter(int responseCode, Response response) throws Exception;
}
