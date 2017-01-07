package ch.viascom.groundwork.restclient.http.request.filter.response;

import ch.viascom.groundwork.restclient.http.request.Request;
import ch.viascom.groundwork.restclient.response.generic.Response;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface ResponseFilter extends ch.viascom.groundwork.restclient.filter.response.ResponseFilter {
    void filter(Response response, Request request) throws Exception;
}
