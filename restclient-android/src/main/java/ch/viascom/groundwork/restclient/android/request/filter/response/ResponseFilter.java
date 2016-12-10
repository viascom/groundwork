package ch.viascom.groundwork.restclient.android.request.filter.response;


import ch.viascom.groundwork.restclient.android.request.Request;
import ch.viascom.groundwork.restclient.response.generic.Response;

/**
 * @author patrick.boesch@viascom.ch, nikola.stankovic@viascom.ch
 */
public interface ResponseFilter extends ch.viascom.groundwork.restclient.filter.response.ResponseFilter {
    void filter(Response response, Request request) throws Exception;
}
