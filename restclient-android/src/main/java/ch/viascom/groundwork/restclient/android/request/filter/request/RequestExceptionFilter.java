package ch.viascom.groundwork.restclient.android.request.filter.request;

import ch.viascom.groundwork.restclient.android.request.Request;

/**
 * @author patrick.boesch@viascom.ch, nikola.stankovic@viascom.ch
 */
public interface RequestExceptionFilter extends ch.viascom.groundwork.restclient.filter.request.RequestExceptionFilter {

    void filter(Exception error, okhttp3.Request httpRequest, Request request) throws Exception;

}
