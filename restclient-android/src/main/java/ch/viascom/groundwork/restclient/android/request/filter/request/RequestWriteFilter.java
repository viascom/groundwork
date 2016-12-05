package ch.viascom.groundwork.restclient.android.request.filter.request;

import ch.viascom.groundwork.restclient.android.request.Request;

/**
 * @author patrick.boesch@viascom.ch, nikola.stankovic@viascom.ch
 */
public interface RequestWriteFilter extends ch.viascom.groundwork.restclient.filter.request.RequestWriteFilter {

    void filter(okhttp3.Request httpRequest, Request request) throws Exception;

}
