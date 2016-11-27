package ch.viascom.groundwork.restclient.http;

import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.http.request.simple.SimpleGetRequest;
import ch.viascom.groundwork.restclient.http.request.simple.SimplePostRequest;
import ch.viascom.groundwork.restclient.response.GenericResponse;
import ch.viascom.groundwork.restclient.response.JSONResponse;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * @author patrick.boesch@viascom.ch
 */
public class ClientTest {

    @Test
    public void clientTest() throws RESTClientException, IOException {

        String url = "http://httpbin.org";
        String path = "/get";

        SimpleGetRequest<JSONResponse> request = new SimpleGetRequest<>(url, JSONResponse.class);
        request.setMediaType("application/json");
        request.setPath(path);
        JSONResponse response = request.execute();
        System.out.println(response.getResponseHeader());
        System.out.println(response.getJson());
    }

    @Test
    public void clientTestShort() throws RESTClientException, IOException {
        GenericResponse response = new SimplePostRequest<>("http://httpbin.org/post", GenericResponse.class).execute();
        System.out.println(response.getResponseHeader());
        System.out.println(EntityUtils.toString((HttpEntity) response.getEntity()));
    }
}
