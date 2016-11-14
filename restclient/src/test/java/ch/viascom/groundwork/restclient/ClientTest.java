package ch.viascom.groundwork.restclient;

import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.request.SimpleGetRequest;
import ch.viascom.groundwork.restclient.response.GenericResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * @author patrick.boesch@viascom.ch
 */
public class ClientTest {

    @Test
    public void clientTest() throws RESTClientException, IOException {

        String url = "https://jsonplaceholder.typicode.com/posts/1";
        String path = "";
        HttpClient httpClient = HttpClientBuilder.create().build();

        GenericResponse test = new SimpleGetRequest(httpClient, url, path).execute();
        System.out.println(EntityUtils.toString(test.getEntity()));
    }
}
