package ch.viascom.groundwork.restclient.http;

import ch.viascom.groundwork.restclient.http.request.simple.SimpleDeleteRequest;
import ch.viascom.groundwork.restclient.http.request.simple.SimpleGetRequest;
import ch.viascom.groundwork.restclient.http.request.simple.SimplePostRequest;
import ch.viascom.groundwork.restclient.http.request.simple.SimplePutRequest;
import ch.viascom.groundwork.restclient.response.JSONResponse;
import org.apache.http.Consts;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
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
        assertThat(request_1.getMediaType()).isEqualTo(ContentType.APPLICATION_JSON.toString());
        assertThat(request_1.getParameterClass()).isEqualTo(JSONResponse.class);

        SimpleGetRequest<JSONResponse> request_2 = new SimpleGetRequest<>(
                "http://httpbin.org/get",
                ContentType.APPLICATION_JSON.toString(),
                JSONResponse.class
        );
        JSONResponse response_2 = request_2.execute();
        assertThat(response_2.getJson()).isNotEmpty();
        assertThat(request_2.getMediaType()).isEqualTo(ContentType.APPLICATION_JSON.toString());
        assertThat(request_2.getParameterClass()).isEqualTo(JSONResponse.class);


        HttpClient httpClient_1 = HttpClientBuilder.create().build();
        SimpleGetRequest<JSONResponse> request_3 = new SimpleGetRequest<>(
                "http://httpbin.org/get",
                ContentType.APPLICATION_JSON.toString(),
                httpClient_1,
                JSONResponse.class
        );
        JSONResponse response_3 = request_3.execute();
        assertThat(response_3.getJson()).isNotEmpty();
        assertThat(request_3.getMediaType()).isEqualTo(ContentType.APPLICATION_JSON.toString());
        assertThat(request_3.getParameterClass()).isEqualTo(JSONResponse.class);
        assertThat(request_3.getHttpClient()).isEqualTo(httpClient_1);
        assertThat(request_3.getPath()).isEqualTo("");

        HttpClient httpClient_2 = HttpClientBuilder.create().build();
        SimpleGetRequest<JSONResponse> request_4 = new SimpleGetRequest<>(
                "http://httpbin.org",
                "/get",
                ContentType.APPLICATION_JSON.toString(),
                httpClient_2,
                JSONResponse.class
        );
        JSONResponse response_4 = request_4.execute();
        assertThat(response_4.getJson()).isNotEmpty();
        assertThat(request_4.getMediaType()).isEqualTo(ContentType.APPLICATION_JSON.toString());
        assertThat(request_4.getParameterClass()).isEqualTo(JSONResponse.class);
        assertThat(request_4.getHttpClient()).isEqualTo(httpClient_2);
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
        assertThat(request_1.getMediaType()).isEqualTo(ContentType.APPLICATION_JSON.toString());
        assertThat(request_1.getParameterClass()).isEqualTo(JSONResponse.class);

        SimplePostRequest<JSONResponse> request_2 = new SimplePostRequest<>(
                "http://httpbin.org/post",
                new StringEntity("POST", Consts.UTF_8),
                JSONResponse.class
        );
        JSONResponse response_2 = request_2.execute();
        assertThat(response_2.getJson()).isNotEmpty();
        assertThat(request_2.getMediaType()).isEqualTo(ContentType.APPLICATION_JSON.toString());
        assertThat(request_2.getParameterClass()).isEqualTo(JSONResponse.class);

        SimplePostRequest<JSONResponse> request_3 = new SimplePostRequest<>(
                "http://httpbin.org/post",
                ContentType.APPLICATION_JSON.toString(),
                new StringEntity("POST", Consts.UTF_8),
                JSONResponse.class
        );
        JSONResponse response_3 = request_3.execute();
        assertThat(response_3.getJson()).isNotEmpty();
        assertThat(request_3.getMediaType()).isEqualTo(ContentType.APPLICATION_JSON.toString());
        assertThat(request_3.getParameterClass()).isEqualTo(JSONResponse.class);
        assertThat(request_3.getPath()).isEqualTo("");

        HttpClient httpClient_1 = HttpClientBuilder.create().build();
        SimplePostRequest<JSONResponse> request_4 = new SimplePostRequest<>(
                "http://httpbin.org/post",
                ContentType.APPLICATION_JSON.toString(),
                new StringEntity("POST", Consts.UTF_8),
                httpClient_1,
                JSONResponse.class
        );
        JSONResponse response_4 = request_4.execute();
        assertThat(response_4.getJson()).isNotEmpty();
        assertThat(request_4.getMediaType()).isEqualTo(ContentType.APPLICATION_JSON.toString());
        assertThat(request_4.getParameterClass()).isEqualTo(JSONResponse.class);
        assertThat(request_4.getHttpClient()).isEqualTo(httpClient_1);
        assertThat(request_4.getPath()).isEqualTo("");

        HttpClient httpClient_2 = HttpClientBuilder.create().build();
        SimplePostRequest<JSONResponse> request_5 = new SimplePostRequest<>(
                "http://httpbin.org",
                "/post",
                ContentType.APPLICATION_JSON.toString(),
                new StringEntity("POST", Consts.UTF_8),
                httpClient_2,
                JSONResponse.class
        );
        JSONResponse response_5 = request_5.execute();
        assertThat(response_5.getJson()).isNotEmpty();
        assertThat(request_5.getMediaType()).isEqualTo(ContentType.APPLICATION_JSON.toString());
        assertThat(request_5.getParameterClass()).isEqualTo(JSONResponse.class);
        assertThat(request_5.getHttpClient()).isEqualTo(httpClient_2);
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
        assertThat(request_1.getMediaType()).isEqualTo(ContentType.APPLICATION_JSON.toString());
        assertThat(request_1.getParameterClass()).isEqualTo(JSONResponse.class);

        SimplePutRequest<JSONResponse> request_2 = new SimplePutRequest<>(
                "http://httpbin.org/put",
                new StringEntity("PUT", Consts.UTF_8),
                JSONResponse.class
        );
        JSONResponse response_2 = request_2.execute();
        assertThat(response_2.getJson()).isNotEmpty();
        assertThat(request_2.getMediaType()).isEqualTo(ContentType.APPLICATION_JSON.toString());
        assertThat(request_2.getParameterClass()).isEqualTo(JSONResponse.class);

        SimplePutRequest<JSONResponse> request_3 = new SimplePutRequest<>(
                "http://httpbin.org/put",
                ContentType.APPLICATION_JSON.toString(),
                new StringEntity("PUT", Consts.UTF_8),
                JSONResponse.class
        );
        JSONResponse response_3 = request_3.execute();
        assertThat(response_3.getJson()).isNotEmpty();
        assertThat(request_3.getMediaType()).isEqualTo(ContentType.APPLICATION_JSON.toString());
        assertThat(request_3.getParameterClass()).isEqualTo(JSONResponse.class);
        assertThat(request_3.getPath()).isEqualTo("");

        HttpClient httpClient_1 = HttpClientBuilder.create().build();
        SimplePutRequest<JSONResponse> request_4 = new SimplePutRequest<>(
                "http://httpbin.org/put",
                ContentType.APPLICATION_JSON.toString(),
                new StringEntity("PUT", Consts.UTF_8),
                httpClient_1,
                JSONResponse.class
        );
        JSONResponse response_4 = request_4.execute();
        assertThat(response_4.getJson()).isNotEmpty();
        assertThat(request_4.getMediaType()).isEqualTo(ContentType.APPLICATION_JSON.toString());
        assertThat(request_4.getParameterClass()).isEqualTo(JSONResponse.class);
        assertThat(request_4.getHttpClient()).isEqualTo(httpClient_1);
        assertThat(request_4.getPath()).isEqualTo("");

        HttpClient httpClient_2 = HttpClientBuilder.create().build();
        SimplePutRequest<JSONResponse> request_5 = new SimplePutRequest<>(
                "http://httpbin.org",
                "/put",
                ContentType.APPLICATION_JSON.toString(),
                new StringEntity("PUT", Consts.UTF_8),
                httpClient_2,
                JSONResponse.class
        );
        JSONResponse response_5 = request_5.execute();
        assertThat(response_5.getJson()).isNotEmpty();
        assertThat(request_5.getMediaType()).isEqualTo(ContentType.APPLICATION_JSON.toString());
        assertThat(request_5.getParameterClass()).isEqualTo(JSONResponse.class);
        assertThat(request_5.getHttpClient()).isEqualTo(httpClient_2);
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
        assertThat(request_1.getMediaType()).isEqualTo(ContentType.APPLICATION_JSON.toString());
        assertThat(request_1.getParameterClass()).isEqualTo(JSONResponse.class);

        SimpleDeleteRequest<JSONResponse> request_2 = new SimpleDeleteRequest<>(
                "http://httpbin.org/delete",
                ContentType.APPLICATION_JSON.toString(),
                JSONResponse.class
        );
        JSONResponse response_2 = request_2.execute();
        assertThat(response_2.getJson()).isNotEmpty();
        assertThat(request_2.getMediaType()).isEqualTo(ContentType.APPLICATION_JSON.toString());
        assertThat(request_2.getParameterClass()).isEqualTo(JSONResponse.class);


        HttpClient httpClient_1 = HttpClientBuilder.create().build();
        SimpleDeleteRequest<JSONResponse> request_3 = new SimpleDeleteRequest<>(
                "http://httpbin.org/delete",
                ContentType.APPLICATION_JSON.toString(),
                httpClient_1,
                JSONResponse.class
        );
        JSONResponse response_3 = request_3.execute();
        assertThat(response_3.getJson()).isNotEmpty();
        assertThat(request_3.getMediaType()).isEqualTo(ContentType.APPLICATION_JSON.toString());
        assertThat(request_3.getParameterClass()).isEqualTo(JSONResponse.class);
        assertThat(request_3.getHttpClient()).isEqualTo(httpClient_1);
        assertThat(request_3.getPath()).isEqualTo("");

        HttpClient httpClient_2 = HttpClientBuilder.create().build();
        SimpleDeleteRequest<JSONResponse> request_4 = new SimpleDeleteRequest<>(
                "http://httpbin.org",
                "/delete",
                ContentType.APPLICATION_JSON.toString(),
                httpClient_2,
                JSONResponse.class
        );
        JSONResponse response_4 = request_4.execute();
        assertThat(response_4.getJson()).isNotEmpty();
        assertThat(request_4.getMediaType()).isEqualTo(ContentType.APPLICATION_JSON.toString());
        assertThat(request_4.getParameterClass()).isEqualTo(JSONResponse.class);
        assertThat(request_4.getHttpClient()).isEqualTo(httpClient_2);
        assertThat(request_4.getPath()).isEqualTo("/delete");
    }
}
