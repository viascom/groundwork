package ch.viascom.groundwork.restclient.http;

import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.http.request.simple.SimpleGetRequest;
import ch.viascom.groundwork.restclient.response.GenericResponse;
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

        String url = "https://jsonplaceholder.typicode.com";
        String path = "/posts/1";

        SimpleGetRequest request = new SimpleGetRequest(url);
        request.setMediaType("application/json");
        request.setPath(path);
        GenericResponse response = request.execute();
        System.out.println(response.getResponseHeader());
        System.out.println(EntityUtils.toString((HttpEntity) response.getEntity()));
    }

    @Test
    public void clientTestShort() throws RESTClientException, IOException {
        GenericResponse response = new SimpleGetRequest("https://jsonplaceholder.typicode.com/posts/1","application/json").execute();
        System.out.println(response.getResponseHeader());
        System.out.println(EntityUtils.toString((HttpEntity) response.getEntity()));
    }
}
