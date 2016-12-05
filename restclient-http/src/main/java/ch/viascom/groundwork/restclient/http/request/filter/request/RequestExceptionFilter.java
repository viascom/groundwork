package ch.viascom.groundwork.restclient.http.request.filter.request;

import ch.viascom.groundwork.restclient.http.request.Request;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface RequestExceptionFilter extends ch.viascom.groundwork.restclient.filter.request.RequestExceptionFilter {

    void filter(Exception error, HttpRequestBase httpRequest, Request request) throws Exception;

}
