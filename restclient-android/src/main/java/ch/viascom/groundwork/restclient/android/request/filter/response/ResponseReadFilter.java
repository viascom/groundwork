package ch.viascom.groundwork.restclient.android.request.filter.response;


import ch.viascom.groundwork.restclient.android.request.Request;

/**
 * @author patrick.boesch@viascom.ch, nikola.stankovic@viascom.ch
 */
public interface ResponseReadFilter extends ch.viascom.groundwork.restclient.filter.response.ResponseReadFilter{

    void filter(okhttp3.Response httpResponse, Request request) throws Exception;

}
