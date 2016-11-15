package ch.viascom.groundwork.restclient.http;

import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.http.request.simple.SimpleGetRequest;
import ch.viascom.groundwork.restclient.response.GenericResponse;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
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
        request.setMediaType(MediaType.APPLICATION_JSON_TYPE.getType());
        request.setPath(path);
        GenericResponse test = request.execute();
        System.out.println(test.getResponseHeader());
        System.out.println(EntityUtils.toString((HttpEntity) test.getEntity()));
    }
}
