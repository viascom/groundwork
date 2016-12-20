package ch.viascom.groundwork.foxhttp;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpResponseException;
import ch.viascom.groundwork.foxhttp.models.User;
import ch.viascom.groundwork.foxhttp.response.DefaultServiceResultHasher;
import ch.viascom.groundwork.foxhttp.response.FoxHttpServiceResultParser;
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
            "      \"destination\": \"\",\n" +
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

        User user = new FoxHttpServiceResultParser<User>(foxHttpResponse, new DefaultServiceResultHasher()).getContent(User.class,true);
        assertThat(user.getUsername()).isEqualTo("foxhttp@viascom.ch");
        assertThat(user.getFirstname()).isEqualTo("Fox");
    }

    @Test
    public void serviceResultParseTestNoCheck() throws Exception {
        FoxHttpResponse foxHttpResponse = new FoxHttpResponse(new ByteArrayInputStream(rawBody.getBytes()), new FoxHttpRequest(), 200, new FoxHttpClient());

        User user = new FoxHttpServiceResultParser<User>(foxHttpResponse).getContent(User.class);
        assertThat(user.getUsername()).isEqualTo("foxhttp@viascom.ch");
        assertThat(user.getFirstname()).isEqualTo("Fox");
    }

    @Test
    public void serviceResultParseFailTest() throws Exception {
        FoxHttpResponse foxHttpResponse = new FoxHttpResponse(new ByteArrayInputStream(rawBodyWrongHash.getBytes()), new FoxHttpRequest(), 200, new FoxHttpClient());

        try {
            User user = new FoxHttpServiceResultParser<User>(foxHttpResponse, new DefaultServiceResultHasher()).getContent(User.class, true);
            assertThat(false).isEqualTo(true);
        } catch (FoxHttpResponseException e) {
            assertThat(e.getMessage()).isEqualTo("Hash not Equal!");
        }
    }

    @Test
    public void serviceFaultParseTest() throws Exception {
        FoxHttpResponse foxHttpResponse = new FoxHttpResponse(new ByteArrayInputStream(rawBodyFault.getBytes()), new FoxHttpRequest(), 200, new FoxHttpClient());

        ServiceFault serviceFault = new FoxHttpServiceResultParser<User>(foxHttpResponse, new DefaultServiceResultHasher()).getFault(true);
        assertThat(serviceFault.getCode()).isEqualTo("F-6345");
        assertThat(serviceFault.getException()).isEqualTo("NotReadyYetException");
    }

    @Test
    public void serviceFaultParseFailTest() throws Exception {
        FoxHttpResponse foxHttpResponse = new FoxHttpResponse(new ByteArrayInputStream(rawBodyFaultWrongHash.getBytes()), new FoxHttpRequest(), 200, new FoxHttpClient());

        try {
            ServiceFault serviceFault = new FoxHttpServiceResultParser<User>(foxHttpResponse, new DefaultServiceResultHasher()).getFault(true);
            assertThat(false).isEqualTo(true);
        } catch (FoxHttpResponseException e) {
            assertThat(e.getMessage()).isEqualTo("Hash not Equal!");
        }
    }
}
