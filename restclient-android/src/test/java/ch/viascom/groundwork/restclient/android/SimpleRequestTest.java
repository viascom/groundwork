package ch.viascom.groundwork.restclient.android;

import ch.viascom.groundwork.restclient.android.request.simple.SimpleDeleteRequest;
import ch.viascom.groundwork.restclient.android.request.simple.SimpleGetRequest;
import ch.viascom.groundwork.restclient.android.request.simple.SimplePostRequest;
import ch.viascom.groundwork.restclient.android.request.simple.SimplePutRequest;
import ch.viascom.groundwork.restclient.response.JSONResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author patrick.boesch@viascom.ch
 */
public class SimpleRequestTest {

    @Test
    public void getRequest() throws Exception {
        SimpleGetRequest<JSONResponse> request_1 = new SimpleGetRequest<>(
                "http://httpbin.org/get",
                JSONResponse.class
        );
        JSONResponse response_1 = request_1.execute();
        assertThat(response_1.getJson()).isNotEmpty();
        assertThat(request_1.getMediaType()).isEqualTo("application/json");
        assertThat(request_1.getParameterClass()).isEqualTo(JSONResponse.class);

        SimpleGetRequest<JSONResponse> request_2 = new SimpleGetRequest<>(
                "http://httpbin.org/get",
                "application/json",
                JSONResponse.class
        );
        JSONResponse response_2 = request_2.execute();
        assertThat(response_2.getJson()).isNotEmpty();
        assertThat(request_2.getMediaType()).isEqualTo("application/json");
        assertThat(request_2.getParameterClass()).isEqualTo(JSONResponse.class);


        OkHttpClient OkHttpClient_1 = new OkHttpClient();
        SimpleGetRequest<JSONResponse> request_3 = new SimpleGetRequest<>(
                "http://httpbin.org/get",
                "application/json",
                OkHttpClient_1,
                JSONResponse.class
        );
        JSONResponse response_3 = request_3.execute();
        assertThat(response_3.getJson()).isNotEmpty();
        assertThat(request_3.getMediaType()).isEqualTo("application/json");
        assertThat(request_3.getParameterClass()).isEqualTo(JSONResponse.class);
        assertThat(request_3.getHttpClient()).isEqualTo(OkHttpClient_1);
        assertThat(request_3.getPath()).isEqualTo("");

        OkHttpClient OkHttpClient_2 = new OkHttpClient();
        SimpleGetRequest<JSONResponse> request_4 = new SimpleGetRequest<>(
                "http://httpbin.org",
                "/get",
                "application/json",
                OkHttpClient_2,
                JSONResponse.class
        );
        JSONResponse response_4 = request_4.execute();
        assertThat(response_4.getJson()).isNotEmpty();
        assertThat(request_4.getMediaType()).isEqualTo("application/json");
        assertThat(request_4.getParameterClass()).isEqualTo(JSONResponse.class);
        assertThat(request_4.getHttpClient()).isEqualTo(OkHttpClient_2);
        assertThat(request_4.getPath()).isEqualTo("/get");
    }

    @Test
    public void postRequest() throws Exception {
        SimplePostRequest<JSONResponse> request_1 = new SimplePostRequest<>(
                "http://httpbin.org/post",
                JSONResponse.class
        );
        JSONResponse response_1 = request_1.execute();
        assertThat(response_1.getJson()).isNotEmpty();
        assertThat(request_1.getMediaType()).isEqualTo("application/json");
        assertThat(request_1.getParameterClass()).isEqualTo(JSONResponse.class);

        SimplePostRequest<JSONResponse> request_2 = new SimplePostRequest<>(
                "http://httpbin.org/post",
                RequestBody.create(MediaType.parse("application/json"), "POST"),
                JSONResponse.class
        );
        JSONResponse response_2 = request_2.execute();
        assertThat(response_2.getJson()).isNotEmpty();
        assertThat(request_2.getMediaType()).isEqualTo("application/json");
        assertThat(request_2.getParameterClass()).isEqualTo(JSONResponse.class);

        SimplePostRequest<JSONResponse> request_3 = new SimplePostRequest<>(
                "http://httpbin.org/post",
                "application/json",
                RequestBody.create(MediaType.parse("application/json"), "POST"),
                JSONResponse.class
        );
        JSONResponse response_3 = request_3.execute();
        assertThat(response_3.getJson()).isNotEmpty();
        assertThat(request_3.getMediaType()).isEqualTo("application/json");
        assertThat(request_3.getParameterClass()).isEqualTo(JSONResponse.class);
        assertThat(request_3.getPath()).isEqualTo("");

        OkHttpClient OkHttpClient_1 = new OkHttpClient();
        SimplePostRequest<JSONResponse> request_4 = new SimplePostRequest<>(
                "http://httpbin.org/post",
                "application/json",
                RequestBody.create(MediaType.parse("application/json"), "POST"),
                OkHttpClient_1,
                JSONResponse.class
        );
        JSONResponse response_4 = request_4.execute();
        assertThat(response_4.getJson()).isNotEmpty();
        assertThat(request_4.getMediaType()).isEqualTo("application/json");
        assertThat(request_4.getParameterClass()).isEqualTo(JSONResponse.class);
        assertThat(request_4.getHttpClient()).isEqualTo(OkHttpClient_1);
        assertThat(request_4.getPath()).isEqualTo("");

        OkHttpClient OkHttpClient_2 = new OkHttpClient();
        SimplePostRequest<JSONResponse> request_5 = new SimplePostRequest<>(
                "http://httpbin.org",
                "/post",
                "application/json",
                RequestBody.create(MediaType.parse("application/json"), "POST"),
                OkHttpClient_2,
                JSONResponse.class
        );
        JSONResponse response_5 = request_5.execute();
        assertThat(response_5.getJson()).isNotEmpty();
        assertThat(request_5.getMediaType()).isEqualTo("application/json");
        assertThat(request_5.getParameterClass()).isEqualTo(JSONResponse.class);
        assertThat(request_5.getHttpClient()).isEqualTo(OkHttpClient_2);
        assertThat(request_5.getPath()).isEqualTo("/post");

    }

    @Test
    public void putRequest() throws Exception {
        SimplePutRequest<JSONResponse> request_1 = new SimplePutRequest<>(
                "http://httpbin.org/put",
                JSONResponse.class
        );
        JSONResponse response_1 = request_1.execute();
        assertThat(response_1.getJson()).isNotEmpty();
        assertThat(request_1.getMediaType()).isEqualTo("application/json");
        assertThat(request_1.getParameterClass()).isEqualTo(JSONResponse.class);

        SimplePutRequest<JSONResponse> request_2 = new SimplePutRequest<>(
                "http://httpbin.org/put",
                RequestBody.create(MediaType.parse("application/json"), "PUT"),
                JSONResponse.class
        );
        JSONResponse response_2 = request_2.execute();
        assertThat(response_2.getJson()).isNotEmpty();
        assertThat(request_2.getMediaType()).isEqualTo("application/json");
        assertThat(request_2.getParameterClass()).isEqualTo(JSONResponse.class);

        SimplePutRequest<JSONResponse> request_3 = new SimplePutRequest<>(
                "http://httpbin.org/put",
                "application/json",
                RequestBody.create(MediaType.parse("application/json"), "PUT"),
                JSONResponse.class
        );
        JSONResponse response_3 = request_3.execute();
        assertThat(response_3.getJson()).isNotEmpty();
        assertThat(request_3.getMediaType()).isEqualTo("application/json");
        assertThat(request_3.getParameterClass()).isEqualTo(JSONResponse.class);
        assertThat(request_3.getPath()).isEqualTo("");

        OkHttpClient OkHttpClient_1 = new OkHttpClient();
        SimplePutRequest<JSONResponse> request_4 = new SimplePutRequest<>(
                "http://httpbin.org/put",
                "application/json",
                RequestBody.create(MediaType.parse("application/json"), "PUT"),
                OkHttpClient_1,
                JSONResponse.class
        );
        JSONResponse response_4 = request_4.execute();
        assertThat(response_4.getJson()).isNotEmpty();
        assertThat(request_4.getMediaType()).isEqualTo("application/json");
        assertThat(request_4.getParameterClass()).isEqualTo(JSONResponse.class);
        assertThat(request_4.getHttpClient()).isEqualTo(OkHttpClient_1);
        assertThat(request_4.getPath()).isEqualTo("");

        OkHttpClient OkHttpClient_2 = new OkHttpClient();
        SimplePutRequest<JSONResponse> request_5 = new SimplePutRequest<>(
                "http://httpbin.org",
                "/put",
                "application/json",
                RequestBody.create(MediaType.parse("application/json"), "PUT"),
                OkHttpClient_2,
                JSONResponse.class
        );
        JSONResponse response_5 = request_5.execute();
        assertThat(response_5.getJson()).isNotEmpty();
        assertThat(request_5.getMediaType()).isEqualTo("application/json");
        assertThat(request_5.getParameterClass()).isEqualTo(JSONResponse.class);
        assertThat(request_5.getHttpClient()).isEqualTo(OkHttpClient_2);
        assertThat(request_5.getPath()).isEqualTo("/put");
    }

    @Test
    public void deleteRequest() throws Exception {
        SimpleDeleteRequest<JSONResponse> request_1 = new SimpleDeleteRequest<>(
                "http://httpbin.org/delete",
                JSONResponse.class
        );
        JSONResponse response_1 = request_1.execute();
        assertThat(response_1.getJson()).isNotEmpty();
        assertThat(request_1.getMediaType()).isEqualTo("application/json");
        assertThat(request_1.getParameterClass()).isEqualTo(JSONResponse.class);

        SimpleDeleteRequest<JSONResponse> request_2 = new SimpleDeleteRequest<>(
                "http://httpbin.org/delete",
                "application/json",
                JSONResponse.class
        );
        JSONResponse response_2 = request_2.execute();
        assertThat(response_2.getJson()).isNotEmpty();
        assertThat(request_2.getMediaType()).isEqualTo("application/json");
        assertThat(request_2.getParameterClass()).isEqualTo(JSONResponse.class);


        OkHttpClient OkHttpClient_1 = new OkHttpClient();
        SimpleDeleteRequest<JSONResponse> request_3 = new SimpleDeleteRequest<>(
                "http://httpbin.org/delete",
                "application/json",
                OkHttpClient_1,
                JSONResponse.class
        );
        JSONResponse response_3 = request_3.execute();
        assertThat(response_3.getJson()).isNotEmpty();
        assertThat(request_3.getMediaType()).isEqualTo("application/json");
        assertThat(request_3.getParameterClass()).isEqualTo(JSONResponse.class);
        assertThat(request_3.getHttpClient()).isEqualTo(OkHttpClient_1);
        assertThat(request_3.getPath()).isEqualTo("");

        OkHttpClient OkHttpClient_2 = new OkHttpClient();
        SimpleDeleteRequest<JSONResponse> request_4 = new SimpleDeleteRequest<>(
                "http://httpbin.org",
                "/delete",
                "application/json",
                OkHttpClient_2,
                JSONResponse.class
        );
        JSONResponse response_4 = request_4.execute();
        assertThat(response_4.getJson()).isNotEmpty();
        assertThat(request_4.getMediaType()).isEqualTo("application/json");
        assertThat(request_4.getParameterClass()).isEqualTo(JSONResponse.class);
        assertThat(request_4.getHttpClient()).isEqualTo(OkHttpClient_2);
        assertThat(request_4.getPath()).isEqualTo("/delete");
    }
}
