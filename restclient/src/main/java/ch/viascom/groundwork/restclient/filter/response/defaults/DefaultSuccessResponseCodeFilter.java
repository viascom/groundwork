package ch.viascom.groundwork.restclient.filter.response.defaults;

import ch.viascom.groundwork.restclient.filter.response.SuccessResponseCodeFilter;
import ch.viascom.groundwork.restclient.response.generic.Response;

/**
 * @author patrick.boesch@viascom.ch
 */
public class DefaultSuccessResponseCodeFilter implements SuccessResponseCodeFilter {

    @Override
    public void filter(int responseCode, Response response) throws Exception {

    }
}
