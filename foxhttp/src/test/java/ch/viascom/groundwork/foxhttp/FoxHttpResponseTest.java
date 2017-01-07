package ch.viascom.groundwork.foxhttp;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpResponseException;
import ch.viascom.groundwork.foxhttp.models.GetResponse;
import ch.viascom.groundwork.foxhttp.type.RequestType;
import org.junit.Test;

import java.net.URL;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpResponseTest {

    private String endpoint = "http://httpbin.org/";

    @Test
    public void responseParserException() throws Exception {

        FoxHttpClient foxHttpClient = new FoxHttpClient();

        FoxHttpRequest foxHttpRequest = new FoxHttpRequest(foxHttpClient);
        foxHttpRequest.setUrl(new URL(endpoint + "get"));
        foxHttpRequest.setRequestType(RequestType.GET);
        foxHttpRequest.setFollowRedirect(true);

        FoxHttpResponse foxHttpResponse = foxHttpRequest.execute();

        try {
            foxHttpResponse.getParsedBody(GetResponse.class);
            assertThat(false).isEqualTo(true);
        } catch (FoxHttpResponseException e) {
            assertThat(e.getMessage()).isEqualTo("getParsedBody needs a FoxHttpResponseParser to deserialize the body");
        }

    }

    @Test
    public void responseToString() throws Exception {

        FoxHttpClient foxHttpClient = new FoxHttpClient();

        FoxHttpRequest foxHttpRequest = new FoxHttpRequest(foxHttpClient);
        foxHttpRequest.setUrl(new URL(endpoint + "get"));
        foxHttpRequest.setRequestType(RequestType.GET);
        foxHttpRequest.setFollowRedirect(true);

        FoxHttpResponse foxHttpResponse = foxHttpRequest.execute();


        assertThat(foxHttpResponse.toString(true).length() > 0).isEqualTo(true);


    }
}
