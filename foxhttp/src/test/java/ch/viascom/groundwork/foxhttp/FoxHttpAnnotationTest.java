package ch.viascom.groundwork.foxhttp;

import ch.viascom.groundwork.foxhttp.annotation.processor.FoxHttpAnnotationParser;
import ch.viascom.groundwork.foxhttp.body.request.RequestStringBody;
import ch.viascom.groundwork.foxhttp.builder.FoxHttpClientBuilder;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.header.HeaderEntry;
import ch.viascom.groundwork.foxhttp.interfaces.FoxHttpExceptionInterfaceTest;
import ch.viascom.groundwork.foxhttp.interfaces.FoxHttpInterfaceTest;
import ch.viascom.groundwork.foxhttp.log.SystemOutFoxHttpLogger;
import ch.viascom.groundwork.foxhttp.models.GetResponse;
import ch.viascom.groundwork.foxhttp.models.PostResponse;
import ch.viascom.groundwork.foxhttp.parser.GsonParser;
import ch.viascom.groundwork.foxhttp.util.NamedInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpAnnotationTest {

    private String endpoint = "http://httpbin.org/";
    private String sslEndpoint = "https://httpbin.org/";

    @Test
    public void get() throws Exception {
        //Set Gson parser, register placeholder
        FoxHttpClientBuilder foxHttpClientBuilder = new FoxHttpClientBuilder()
                .setFoxHttpResponseParser(new GsonParser())
                .addFoxHttpPlaceholderEntry("host", endpoint)
                .setFoxHttpLogger(new SystemOutFoxHttpLogger(true,"TEST"));

        //Request
        FoxHttpInterfaceTest foxHttpInterfaceTest = new FoxHttpAnnotationParser().parseInterface(FoxHttpInterfaceTest.class, foxHttpClientBuilder.build());
        GetResponse getResponse = foxHttpInterfaceTest.get("1345");

        assertThat(getResponse.getArgs().get("key")).isEqualTo("1345");
    }

    @Test
    public void bigGet() throws Exception {
        //Set Gson parser, register placeholder
        FoxHttpClientBuilder foxHttpClientBuilder = new FoxHttpClientBuilder()
                .setFoxHttpResponseParser(new GsonParser())
                .addFoxHttpPlaceholderEntry("host", endpoint);


        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key", "ABC");
        hashMap.put("version", "v1.0");
        hashMap.put("date", "01.01.2000");


        //Request
        FoxHttpInterfaceTest foxHttpInterfaceTest = new FoxHttpAnnotationParser().parseInterface(FoxHttpInterfaceTest.class, foxHttpClientBuilder.build());
        GetResponse getResponse = foxHttpInterfaceTest.bigGet(hashMap, "FoxHttp");

        assertThat(getResponse.getArgs().get("key")).isEqualTo("ABC");
        assertThat(getResponse.getArgs().get("version")).isEqualTo("v1.0");
        assertThat(getResponse.getArgs().get("date")).isEqualTo("01.01.2000");
        assertThat(getResponse.getHeaders().get("Product")).isEqualTo("FoxHttp");

    }

    @Test
    public void getRequest() throws Exception {
        //Set Gson parser, register placeholder
        FoxHttpClientBuilder foxHttpClientBuilder = new FoxHttpClientBuilder()
                .setFoxHttpResponseParser(new GsonParser())
                .addFoxHttpPlaceholderEntry("host", endpoint);

        FoxHttpInterfaceTest foxHttpInterfaceTest = new FoxHttpAnnotationParser().parseInterface(FoxHttpInterfaceTest.class, foxHttpClientBuilder.build());
        FoxHttpRequest request = foxHttpInterfaceTest.getRequest("get");

        assertThat(request.getUrl().toString()).isEqualTo(endpoint + "{path}");
        FoxHttpResponse response = request.execute();
        assertThat(response.getFoxHttpRequest().getUrl().toString()).isEqualTo(endpoint + "get");

    }


    @Test
    public void postString() throws Exception {
        //Set Gson parser, register placeholder
        FoxHttpClientBuilder foxHttpClientBuilder = new FoxHttpClientBuilder()
                .setFoxHttpResponseParser(new GsonParser())
                .addFoxHttpPlaceholderEntry("host", endpoint);


        ArrayList<HeaderEntry> list = new ArrayList<>();
        list.add(new HeaderEntry("Product", "FoxHttp"));
        list.add(new HeaderEntry("X-Version", "v1.0"));


        //Request
        FoxHttpInterfaceTest foxHttpInterfaceTest = new FoxHttpAnnotationParser().parseInterface(FoxHttpInterfaceTest.class, foxHttpClientBuilder.build());
        PostResponse postResponse = foxHttpInterfaceTest.postBody(new RequestStringBody("Post Annotation Test"), list);

        assertThat(postResponse.getData()).isEqualTo("Post Annotation Test");
        assertThat(postResponse.getHeaders().get("X-Version")).isEqualTo("v1.0");
        assertThat(postResponse.getHeaders().get("Product")).isEqualTo("FoxHttp");

    }

    @Test
    public void postForm() throws Exception {
        //Set Gson parser, register placeholder
        FoxHttpClientBuilder foxHttpClientBuilder = new FoxHttpClientBuilder()
                .setFoxHttpResponseParser(new GsonParser())
                .addFoxHttpPlaceholderEntry("host", endpoint);


        HashMap<String, String> headers = new HashMap<>();
        headers.put("Product", "FoxHttp");
        headers.put("X-Version", "v1.0");

        //Request
        FoxHttpInterfaceTest foxHttpInterfaceTest = new FoxHttpAnnotationParser().parseInterface(FoxHttpInterfaceTest.class, foxHttpClientBuilder.build());
        PostResponse postResponse = foxHttpInterfaceTest.postForm("foxhttp", "123456789", headers);

        assertThat(postResponse.getForm().get("username")).isEqualTo("foxhttp");
        assertThat(postResponse.getForm().get("password")).isEqualTo("123456789");
        assertThat(postResponse.getHeaders().get("X-Version")).isEqualTo("v1.0");
        assertThat(postResponse.getHeaders().get("Product")).isEqualTo("FoxHttp");
    }

    @Test
    public void postFormMap() throws Exception {
        //Set Gson parser, register placeholder
        FoxHttpClientBuilder foxHttpClientBuilder = new FoxHttpClientBuilder()
                .setFoxHttpResponseParser(new GsonParser())
                .addFoxHttpPlaceholderEntry("host", endpoint);

        HashMap<String, String> form = new HashMap<>();
        form.put("username", "foxhttp");
        form.put("password", "123456789");

        //Request
        FoxHttpInterfaceTest foxHttpInterfaceTest = new FoxHttpAnnotationParser().parseInterface(FoxHttpInterfaceTest.class, foxHttpClientBuilder.build());
        PostResponse postResponse = foxHttpInterfaceTest.postFormMap(form);

        assertThat(postResponse.getForm().get("username")).isEqualTo("foxhttp");
        assertThat(postResponse.getForm().get("password")).isEqualTo("123456789");
    }

    @Test
    public void postMulti() throws Exception {
        //Set Gson parser, register placeholder
        FoxHttpClientBuilder foxHttpClientBuilder = new FoxHttpClientBuilder()
                .setFoxHttpResponseParser(new GsonParser())
                .addFoxHttpPlaceholderEntry("host", endpoint);

        String jsonString = "{\"key\":\"1234\"}";

        NamedInputStream namedInputStream = new NamedInputStream("json", new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8)), "UTF-8", "application/json");

        //Request
        FoxHttpInterfaceTest foxHttpInterfaceTest = new FoxHttpAnnotationParser().parseInterface(FoxHttpInterfaceTest.class, foxHttpClientBuilder.build());
        FoxHttpResponse foxHttpResponse = foxHttpInterfaceTest.postMulti("Text", namedInputStream);

        assertThat(foxHttpResponse.getParsedBody(PostResponse.class).getForm().get("test")).isEqualTo("Text");
        assertThat(foxHttpResponse.getParsedBody(PostResponse.class).getFiles().get("stream")).isEqualTo("{\"key\":\"1234\"}");
    }

    @Test
    public void postMultiMap() throws Exception {
        //Set Gson parser, register placeholder
        FoxHttpClientBuilder foxHttpClientBuilder = new FoxHttpClientBuilder()
                .setFoxHttpResponseParser(new GsonParser())
                .addFoxHttpPlaceholderEntry("host", endpoint);

        String jsonString = "{\"key\":\"1234\"}";

        NamedInputStream namedInputStream = new NamedInputStream("json", new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8)), "UTF-8", "application/json");

        HashMap<String, NamedInputStream> namedInputStreamMap = new HashMap<>();
        namedInputStreamMap.put("stream", namedInputStream);


        //Request
        FoxHttpInterfaceTest foxHttpInterfaceTest = new FoxHttpAnnotationParser().parseInterface(FoxHttpInterfaceTest.class, foxHttpClientBuilder.build());
        FoxHttpResponse foxHttpResponse = foxHttpInterfaceTest.postMultiMap(namedInputStreamMap);

        assertThat(foxHttpResponse.getParsedBody(PostResponse.class).getFiles().get("stream")).isEqualTo("{\"key\":\"1234\"}");
    }

    @Test
    public void bodyInGetException() throws Exception {
        try {
            //Set Gson parser, register placeholder
            FoxHttpClientBuilder foxHttpClientBuilder = new FoxHttpClientBuilder()
                    .setFoxHttpResponseParser(new GsonParser())
                    .addFoxHttpPlaceholderEntry("host", endpoint);

            FoxHttpExceptionInterfaceTest foxHttpInterfaceTest = new FoxHttpAnnotationParser().parseInterface(FoxHttpExceptionInterfaceTest.class, foxHttpClientBuilder.build());
            assertThat(false).isEqualTo(true);
        } catch (FoxHttpRequestException e) {
            assertThat(e.getMessage()).isEqualTo("FoxHttpExceptionInterfaceTest.bodyInGet\n" +
                    "-> Non-body HTTP method cannot contain @Body, @Field, @FieldMap, @Part or @PartMap.");
        }
    }
}
