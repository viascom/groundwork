package ch.viascom.groundwork.foxhttp;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpResponseException;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseInterceptorContext;
import ch.viascom.groundwork.foxhttp.models.User;
import ch.viascom.groundwork.foxhttp.response.serviceresult.DefaultServiceResultFaultInterceptor;
import ch.viascom.groundwork.foxhttp.response.serviceresult.DefaultServiceResultHasher;
import ch.viascom.groundwork.foxhttp.response.serviceresult.FoxHttpServiceResultException;
import ch.viascom.groundwork.foxhttp.response.serviceresult.FoxHttpServiceResultResponse;
import ch.viascom.groundwork.serviceresult.ServiceResultStatus;
import ch.viascom.groundwork.serviceresult.exception.ServiceFault;
import org.junit.Test;

import java.io.ByteArrayInputStream;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpServiceResultTest {

    private String rawBody = "{\n" +
            "      \"status\": \"successful\",\n" +
            "      \"type\": \"ch.viascom.groundwork.foxhttp.models.User\",\n" +
            "      \"content\": {\n" +
            "        \"username\": \"foxhttp@viascom.ch\",\n" +
            "        \"firstname\": \"Fox\",\n" +
            "        \"lastname\": \"Http\"\n" +
            "      },\n" +
            "      \"hash\": \"86d536d3ac63d8e0e81415f7efb8e9661388048\",\n" +
            "      \"destination\": \"ch.viascom.groundwork.foxhttp:method\",\n" +
            "      \"metadata\": {}\n" +
            "  }\n";

    private String rawBodyWrongHash = "{\n" +
            "      \"status\": \"successful\",\n" +
            "      \"type\": \"ch.viascom.groundwork.foxhttp.models.User\",\n" +
            "      \"content\": {\n" +
            "        \"username\": \"foxhttp@viascom.ch\",\n" +
            "        \"firstname\": \"Fox\",\n" +
            "        \"lastname\": \"Http\"\n" +
            "      },\n" +
            "      \"hash\": \"WRONGHASH\",\n" +
            "      \"destination\": \"\",\n" +
            "      \"metadata\": {}\n" +
            "  }\n";

    private String rawBodyFault = "{\n" +
            "      \"status\": \"successful\",\n" +
            "      \"type\": \"ch.viascom.groundwork.serviceresult.exception.ServiceFault\",\n" +
            "      \"content\": {\n" +
            "        \"code\": \"F-6345\",\n" +
            "        \"message\": \"Fox is not ready yet!\",\n" +
            "        \"requestUrl\": \"http://localhost/fault.json\",\n" +
            "        \"requestedType\": \"GET\",\n" +
            "        \"responseStatusCode\": \"500\",\n" +
            "        \"requestParams\": [],\n" +
            "        \"exception\": \"NotReadyYetException\"\n" +
            "      },\n" +
            "      \"hash\": \"9def316512e095ee2f578588e4c492beda88b3b3\",\n" +
            "      \"destination\": \"\",\n" +
            "      \"metadata\": {}\n" +
            "  }\n";

    private String rawBodyFaultWrongHash = "{\n" +
            "      \"status\": \"successful\",\n" +
            "      \"type\": \"ch.viascom.groundwork.serviceresult.exception.ServiceFault\",\n" +
            "      \"content\": {\n" +
            "        \"code\": \"F-6345\",\n" +
            "        \"message\": \"Fox is not ready yet!\",\n" +
            "        \"requestUrl\": \"http://localhost/fault.json\",\n" +
            "        \"requestedType\": \"GET\",\n" +
            "        \"responseStatusCode\": \"500\",\n" +
            "        \"requestParams\": [],\n" +
            "        \"exception\": \"NotReadyYetException\"\n" +
            "      },\n" +
            "      \"hash\": \"WRONGHASH\",\n" +
            "      \"destination\": \"\",\n" +
            "      \"metadata\": {}\n" +
            "  }\n";

    @Test
    public void serviceResultParseTest() throws Exception {
        FoxHttpResponse foxHttpResponse = new FoxHttpResponse(new ByteArrayInputStream(rawBody.getBytes()), new FoxHttpRequest(), 200, new FoxHttpClient());

        FoxHttpServiceResultResponse resultResponse = new FoxHttpServiceResultResponse(foxHttpResponse, new DefaultServiceResultHasher());

        User user = resultResponse.getContent(User.class, true);
        assertThat(user.getUsername()).isEqualTo("foxhttp@viascom.ch");
        assertThat(user.getFirstname()).isEqualTo("Fox");
        assertThat(resultResponse.getStatus()).isEqualTo(ServiceResultStatus.successful);
        assertThat(resultResponse.getType()).isEqualTo("ch.viascom.groundwork.foxhttp.models.User");
        assertThat(resultResponse.getHash()).isEqualTo("86d536d3ac63d8e0e81415f7efb8e9661388048");
        assertThat(resultResponse.getDestination()).isEqualTo("ch.viascom.groundwork.foxhttp:method");
    }

    @Test
    public void serviceResultParseTestNoCheck() throws Exception {
        FoxHttpResponse foxHttpResponse = new FoxHttpResponse(new ByteArrayInputStream(rawBody.getBytes()), new FoxHttpRequest(), 200, new FoxHttpClient());

        User user = new FoxHttpServiceResultResponse(foxHttpResponse).getContent(User.class);
        assertThat(user.getUsername()).isEqualTo("foxhttp@viascom.ch");
        assertThat(user.getFirstname()).isEqualTo("Fox");
    }

    @Test
    public void serviceResultParseFailTest() throws Exception {
        FoxHttpResponse foxHttpResponse = new FoxHttpResponse(new ByteArrayInputStream(rawBodyWrongHash.getBytes()), new FoxHttpRequest(), 200, new FoxHttpClient());

        try {
            User user = new FoxHttpServiceResultResponse(foxHttpResponse, new DefaultServiceResultHasher()).getContent(User.class, true);
            assertThat(false).isEqualTo(true);
        } catch (FoxHttpResponseException e) {
            assertThat(e.getMessage()).isEqualTo("Hash not Equal!");
        }
    }

    @Test
    public void serviceFaultParseTest() throws Exception {
        FoxHttpResponse foxHttpResponse = new FoxHttpResponse(new ByteArrayInputStream(rawBodyFault.getBytes()), new FoxHttpRequest(), 200, new FoxHttpClient());

        ServiceFault serviceFault = new FoxHttpServiceResultResponse(foxHttpResponse, new DefaultServiceResultHasher()).getFault(true);
        assertThat(serviceFault.getCode()).isEqualTo("F-6345");
        assertThat(serviceFault.getException()).isEqualTo("NotReadyYetException");
    }

    @Test
    public void serviceFaultParseFailTest() throws Exception {
        FoxHttpResponse foxHttpResponse = new FoxHttpResponse(new ByteArrayInputStream(rawBodyFaultWrongHash.getBytes()), new FoxHttpRequest(), 200, new FoxHttpClient());

        try {
            ServiceFault serviceFault = new FoxHttpServiceResultResponse(foxHttpResponse, new DefaultServiceResultHasher()).getFault(true);
            assertThat(false).isEqualTo(true);
        } catch (FoxHttpResponseException e) {
            assertThat(e.getMessage()).isEqualTo("Hash not Equal!");
        }
    }

    @Test
    public void serviceFaultThrowTest() throws Exception {
        FoxHttpResponse foxHttpResponse = new FoxHttpResponse(new ByteArrayInputStream(rawBodyFault.getBytes()), new FoxHttpRequest(), 200, new FoxHttpClient());
        FoxHttpResponseInterceptorContext interceptorContext = new FoxHttpResponseInterceptorContext(500, foxHttpResponse, new FoxHttpRequest(), new FoxHttpClient());

        try {
            new DefaultServiceResultFaultInterceptor().onIntercept(interceptorContext);
            assertThat(false).isEqualTo(true);
        } catch (FoxHttpServiceResultException e) {
            assertThat(e.getServiceFault().getCode()).isEqualTo("F-6345");
        }

    }
}
