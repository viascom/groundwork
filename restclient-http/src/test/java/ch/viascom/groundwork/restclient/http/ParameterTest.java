package ch.viascom.groundwork.restclient.http;

import ch.viascom.groundwork.restclient.http.model.QueryParamReturn;
import ch.viascom.groundwork.restclient.http.request.simple.SimpleGetRequest;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author patrick.boesch@viascom.ch
 */
public class ParameterTest {

    @Test
    public void queryParamTest() throws Exception {

        SimpleGetRequest<QueryParamReturn> request = new SimpleGetRequest<>(
                "http://httpbin.org/response-headers",
                QueryParamReturn.class
        );
        request.addQueryParam("Viascom-Framework", "GroundWork-RESTClient");
        QueryParamReturn response = request.execute();
        assertThat(response.getViascomFramework()).isEqualTo("GroundWork-RESTClient");
    }
}
