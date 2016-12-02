package ch.viascom.groundwork.restclient.android.request.filter.request;


import ch.viascom.groundwork.restclient.android.request.Request;

/**
 * @author patrick.boesch@viascom.ch, nikola.stankovic@viascom.ch
 */
public interface RequestFilter extends ch.viascom.groundwork.restclient.filter.request.RequestFilter {

    void filter(Request request) throws Exception;

}
