package ch.viascom.groundwork.restclient.jaxrs;

import ch.viascom.groundwork.restclient.jaxrs.request.simple.SimpleGetRequest;
import ch.viascom.groundwork.restclient.response.GenericResponse;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

/**
 * @author patrick.boesch@viascom.ch
 */
public class ClientTest {

    @Test
    public void clientTest() throws Exception {

        String url = "https://httpbin.org/ip";
        String path = "";

        SimpleGetRequest request = new SimpleGetRequest(url);
        request.setMediaType(MediaType.APPLICATION_JSON_TYPE.getType());
        request.setPath(path);
        GenericResponse test = request.execute();
        System.out.println(test.getResponseHeader());
        System.out.println(test);
    }
}
