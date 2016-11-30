package ch.viascom.groundwork.restclient.http.request.filter.request;

import ch.viascom.groundwork.restclient.http.request.Request;
import org.apache.http.client.methods.HttpRequestBase;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface RequestWriteFilter extends ch.viascom.groundwork.restclient.filter.request.RequestWriteFilter {

    void filter(HttpRequestBase httpRequest, Request request) throws Exception;

}
