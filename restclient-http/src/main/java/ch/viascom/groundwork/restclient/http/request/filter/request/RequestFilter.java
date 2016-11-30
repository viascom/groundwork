package ch.viascom.groundwork.restclient.http.request.filter.request;

import ch.viascom.groundwork.restclient.http.request.Request;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface RequestFilter extends ch.viascom.groundwork.restclient.filter.request.RequestFilter {

    void filter(Request request) throws Exception;

}
