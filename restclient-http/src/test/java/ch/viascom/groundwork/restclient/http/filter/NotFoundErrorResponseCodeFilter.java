package ch.viascom.groundwork.restclient.http.filter;

import ch.viascom.groundwork.restclient.filter.response.ErrorResponseCodeFilter;
import ch.viascom.groundwork.restclient.response.generic.ErrorResponse;
import ch.viascom.groundwork.restclient.response.generic.Response;

import java.util.NoSuchElementException;

/**
 * @author patrick.boesch@viascom.ch
 */
public class NotFoundErrorResponseCodeFilter implements ErrorResponseCodeFilter {
    @Override
    public void filter(int i, Response response, ErrorResponse errorResponse) throws Exception {
        if (i == 404 || i == 202) {
            throw new NoSuchElementException("Page was not found");
        }
    }
}
