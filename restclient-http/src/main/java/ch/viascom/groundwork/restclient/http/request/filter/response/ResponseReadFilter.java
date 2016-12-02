package ch.viascom.groundwork.restclient.http.request.filter.response;

import ch.viascom.groundwork.restclient.http.request.Request;
import org.apache.http.HttpResponse;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface ResponseReadFilter extends ch.viascom.groundwork.restclient.filter.response.ResponseReadFilter{

    void filter(HttpResponse httpResponse, Request request) throws Exception;

}
