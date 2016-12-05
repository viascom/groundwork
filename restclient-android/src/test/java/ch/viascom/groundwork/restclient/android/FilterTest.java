package ch.viascom.groundwork.restclient.android;

import ch.viascom.groundwork.restclient.android.request.simple.SimpleGetRequest;
import ch.viascom.groundwork.restclient.android.request.simple.filter.BasicAuthFilter;
import ch.viascom.groundwork.restclient.android.request.simple.filter.BearerTokenFilter;
import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.filter.FilterTypes;
import ch.viascom.groundwork.restclient.filter.RESTFilter;
import ch.viascom.groundwork.restclient.android.filter.NotFoundErrorResponseCodeFilter;
import ch.viascom.groundwork.restclient.android.filter.PathRequestFilter;
import ch.viascom.groundwork.restclient.android.model.HeaderResponse;
import ch.viascom.groundwork.restclient.response.JSONResponse;
import ch.viascom.groundwork.restclient.response.NoContentResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FilterTest {

    private String basicAuthResponse = "{\n" +
            "  \"authenticated\": true, \n" +
            "  \"user\": \"viascom\"\n" +
            "}\n";


    @Test
    public void basicAuthFilter() throws Exception {
        SimpleGetRequest<JSONResponse> request = new SimpleGetRequest<>("http://httpbin.org/basic-auth/viascom/groundwork", JSONResponse.class);
        request.register(new BasicAuthFilter("viascom", "groundwork"));
        JSONResponse response = request.execute();
        assertThat(response.getJson()).isEqualTo(basicAuthResponse);
    }

    @Test
    public void basicAuthFilterFail() throws Exception {
        SimpleGetRequest<NoContentResponse> request = new SimpleGetRequest<>("http://httpbin.org/basic-auth/viascom/groundwork", NoContentResponse.class);
        request.register(new BasicAuthFilter("viascom", "WrongPassword"));
        try {
            request.execute();
        } catch (RESTClientException e) {
            assertThat(e.getErrorResponse().getResponseHeader().getStatusCode()).isEqualTo(401);
        }
    }

    @Test
    public void bearerFilterFail() throws Exception {
        SimpleGetRequest<HeaderResponse> request = new SimpleGetRequest<>("http://httpbin.org/headers", HeaderResponse.class);
        request.register(new BearerTokenFilter("SECURE_TOKEN_1234"));
        HeaderResponse response = request.execute();
        assertThat(response.getHeaders().get("Authorization")).isEqualTo("Bearer SECURE_TOKEN_1234");
    }

    @Test
    public void requestFilter() throws Exception {
        SimpleGetRequest<JSONResponse> request = new SimpleGetRequest<>("http://httpbin.org", JSONResponse.class);
        //Set path to "/status/204"
        request.register(new PathRequestFilter());
        JSONResponse response = request.execute();
        assertThat(request.getPath()).isEqualTo("/status/204");
        assertThat(response.getResponseHeader().getStatusCode()).isEqualTo(204);
    }

    @Test
    public void errorResponseCodeFilter() throws Exception {
        SimpleGetRequest<JSONResponse> request = new SimpleGetRequest<>("http://httpbin.org/status/404", JSONResponse.class);
        // Throw NoSuchElementException if response code is 404
        request.register(new NotFoundErrorResponseCodeFilter());
        try {
            request.execute();
        } catch (RESTClientException e) {
            assertThat(e.getCause()).isInstanceOf(NoSuchElementException.class);
        }
    }

    @Test
    public void allowedResponseCodeFilter() throws Exception {
        SimpleGetRequest<JSONResponse> request = new SimpleGetRequest<>("http://httpbin.org/status/404", JSONResponse.class);
        request.addAdditionalAllowedStatusCode(404);
        // Throw NoSuchElementException if response code is 404 and 404 ist not allowed
        request.register(new NotFoundErrorResponseCodeFilter());
        JSONResponse response = request.execute();
        assertThat(response.getResponseHeader().getStatusCode()).isEqualTo(404);
    }

    @Test
    public void deniedResponseCodeFilter() throws Exception {
        SimpleGetRequest<JSONResponse> request = new SimpleGetRequest<>("http://httpbin.org/status/202", JSONResponse.class);
        request.addAdditionalDeniedStatusCode(202);
        // Throw NoSuchElementException if response code is 202 and 202 ist not allowed
        request.register(new NotFoundErrorResponseCodeFilter());
        try {
            request.execute();
        } catch (RESTClientException e) {
            assertThat(e.getCause()).isInstanceOf(NoSuchElementException.class);
        }
    }

    @Test
    public void unregisterFilter() throws Exception {
        SimpleGetRequest<JSONResponse> request = new SimpleGetRequest<>("http://httpbin.org/status/202", JSONResponse.class);
        request.register(new NotFoundErrorResponseCodeFilter());
        request.unregister(NotFoundErrorResponseCodeFilter.class);
        JSONResponse response = request.execute();
        assertThat(response.getResponseHeader().getStatusCode()).isEqualTo(202);
    }

    @Test
    public void notSupportedFilter() throws Exception {
        SimpleGetRequest<JSONResponse> request = new SimpleGetRequest<>("http://httpbin.org/get", JSONResponse.class);
        try {
            request.register(new RESTFilter() {
            });
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage()).isEqualTo("Not supported RESTFilter-Class");
        }
    }

    @Test
    public void getFilters() throws Exception {
        SimpleGetRequest<JSONResponse> request = new SimpleGetRequest<>("http://httpbin.org/get", JSONResponse.class);
        request.register(new NotFoundErrorResponseCodeFilter());

        ArrayList<RESTFilter> restFilters = request.getRESTFilters(FilterTypes.ERRORRESPONSECODEFILTER);
        assertThat(restFilters.get(0)).isInstanceOf(NotFoundErrorResponseCodeFilter.class);
    }

}
