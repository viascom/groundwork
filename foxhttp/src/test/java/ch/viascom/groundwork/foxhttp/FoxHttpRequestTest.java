package ch.viascom.groundwork.foxhttp;

import ch.viascom.groundwork.foxhttp.authorization.BasicAuthAuthorization;
import ch.viascom.groundwork.foxhttp.authorization.BearerTokenAuthorization;
import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationScope;
import ch.viascom.groundwork.foxhttp.body.request.FoxHttpRequestBody;
import ch.viascom.groundwork.foxhttp.body.request.RequestStringBody;
import ch.viascom.groundwork.foxhttp.builder.FoxHttpClientBuilder;
import ch.viascom.groundwork.foxhttp.builder.FoxHttpRequestBuilder;
import ch.viascom.groundwork.foxhttp.cookie.DefaultCookieStore;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.header.FoxHttpRequestHeader;
import ch.viascom.groundwork.foxhttp.log.SystemOutFoxHttpLogger;
import ch.viascom.groundwork.foxhttp.models.*;
import ch.viascom.groundwork.foxhttp.objects.RemoveMeAuthorization;
import ch.viascom.groundwork.foxhttp.parser.GsonParser;
import ch.viascom.groundwork.foxhttp.proxy.FoxHttpProxyStrategy;
import ch.viascom.groundwork.foxhttp.query.FoxHttpRequestQuery;
import ch.viascom.groundwork.foxhttp.type.RequestType;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.HttpCookie;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;


/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpRequestTest {

    private String endpoint = "http://httpbin.org/";
    private String sslEndpoint = "https://httpbin.org/";

    @Test
    public void requestValidateExceptionTest() throws Exception {
        //URL of the request ist not defined
        FoxHttpRequest foxHttpRequestURL = new FoxHttpRequest();

        try {
            foxHttpRequestURL.execute();
            assertThat(false).isEqualTo(true);
        } catch (FoxHttpRequestException e) {
            assertThat(e.getMessage()).isEqualTo("URL of the request ist not defined");
        }

        //FoxHttpClient of the request ist not defined
        FoxHttpRequest foxHttpRequestClient = new FoxHttpRequest(null);
        foxHttpRequestClient.setUrl(new URL(endpoint + "get"));

        try {
            foxHttpRequestClient.execute();
            assertThat(false).isEqualTo(true);
        } catch (FoxHttpRequestException e) {
            assertThat(e.getMessage()).isEqualTo("FoxHttpClient of the request ist not defined");
        }

    }

    @Test
    public void getRequest() throws Exception {

        FoxHttpClient foxHttpClient = new FoxHttpClient();
        foxHttpClient.setFoxHttpResponseParser(new GsonParser());

        FoxHttpRequest foxHttpRequest = new FoxHttpRequest(foxHttpClient);
        foxHttpRequest.setUrl(new URL(endpoint + "get"));
        foxHttpRequest.setRequestType(RequestType.GET);
        foxHttpRequest.setFollowRedirect(true);

        FoxHttpResponse foxHttpResponse = foxHttpRequest.execute();

        assertThat(foxHttpResponse.getResponseCode()).isEqualTo(200);
        assertThat(foxHttpResponse.getByteArrayOutputStreamBody().size()).isGreaterThan(0);
        assertThat(foxHttpResponse.getFoxHttpRequest().getRequestType()).isEqualTo(RequestType.GET);
        assertThat(foxHttpResponse.getFoxHttpRequest().getUrl()).isEqualTo(new URL(endpoint + "get"));

        GetResponse getResponse = foxHttpResponse.getParsedBody(GetResponse.class);

        assertThat(getResponse.getUrl()).isEqualTo(endpoint + "get");
    }

    @Test
    public void getQueryRequest() throws Exception {

        FoxHttpClient foxHttpClient = new FoxHttpClient();
        foxHttpClient.setFoxHttpResponseParser(new GsonParser());

        FoxHttpRequestQuery requestQuery = new FoxHttpRequestQuery();
        requestQuery.addQueryEntry("name", "FoxHttp");

        FoxHttpRequest foxHttpRequest = new FoxHttpRequest(foxHttpClient);
        foxHttpRequest.setUrl(new URL(endpoint + "get"));
        foxHttpRequest.setRequestType(RequestType.GET);
        foxHttpRequest.setRequestQuery(requestQuery);
        foxHttpRequest.setFollowRedirect(true);

        FoxHttpResponse foxHttpResponse = foxHttpRequest.execute();

        assertThat(foxHttpResponse.getResponseCode()).isEqualTo(200);
        assertThat(foxHttpResponse.getByteArrayOutputStreamBody().size()).isGreaterThan(0);

        GetResponse getResponse = foxHttpResponse.getParsedBody(GetResponse.class);

        assertThat(getResponse.getUrl()).isEqualTo(endpoint + "get" + requestQuery.getQueryString());
        assertThat(getResponse.getArgs().get("name")).isEqualTo("FoxHttp");
    }

    @Test
    public void getRequestPlaceholder() throws Exception {

        FoxHttpClient foxHttpClient = new FoxHttpClient();
        foxHttpClient.setFoxHttpResponseParser(new GsonParser());
        foxHttpClient.getFoxHttpPlaceholderStrategy().addPlaceholder("endpoint", endpoint);
        foxHttpClient.setFoxHttpLogger(new SystemOutFoxHttpLogger(true, "test"));

        FoxHttpRequest foxHttpRequest = new FoxHttpRequest(foxHttpClient);
        foxHttpRequest.setUrl("{endpoint}/get");
        foxHttpRequest.setRequestType(RequestType.GET);
        foxHttpRequest.setFollowRedirect(true);

        FoxHttpResponse foxHttpResponse = foxHttpRequest.execute();

        assertThat(foxHttpResponse.getResponseCode()).isEqualTo(200);
        assertThat(foxHttpResponse.getByteArrayOutputStreamBody().size()).isGreaterThan(0);

        GetResponse getResponse = foxHttpResponse.getParsedBody(GetResponse.class);
        assertThat(getResponse.getUrl()).isEqualTo(endpoint + "get");
    }

    @Test
    public void getObjectAsQueryMapRequest() throws Exception {

        FoxHttpClient foxHttpClient = new FoxHttpClient();
        foxHttpClient.setFoxHttpResponseParser(new GsonParser());

        QueryDataHolder queryDataHolder = new QueryDataHolder("FoxHttp", 12, "java");


        FoxHttpRequestQuery requestQuery = new FoxHttpRequestQuery();
        requestQuery.parseObjectAsQueryMap(new ArrayList<>(Arrays.asList("name", "index", "key")), queryDataHolder);

        FoxHttpRequest foxHttpRequest = new FoxHttpRequest(foxHttpClient);
        foxHttpRequest.setUrl(new URL(endpoint + "get"));
        foxHttpRequest.setRequestType(RequestType.GET);
        foxHttpRequest.setRequestQuery(requestQuery);
        foxHttpRequest.setFollowRedirect(true);

        FoxHttpResponse foxHttpResponse = foxHttpRequest.execute();

        assertThat(foxHttpResponse.getResponseCode()).isEqualTo(200);
        assertThat(foxHttpResponse.getByteArrayOutputStreamBody().size()).isGreaterThan(0);

        GetResponse getResponse = foxHttpResponse.getParsedBody(GetResponse.class);

        assertThat(getResponse.getUrl()).isEqualTo(endpoint + "get" + requestQuery.getQueryString());
        assertThat(getResponse.getArgs().get("name")).isEqualTo("FoxHttp");
        assertThat(getResponse.getArgs().get("index")).isEqualTo("12");
        assertThat(getResponse.getArgs().get("key")).isEqualTo("java");
    }


    @Test
    public void getHeaderRequest() throws Exception {

        FoxHttpClient foxHttpClient = new FoxHttpClient();
        foxHttpClient.setFoxHttpResponseParser(new GsonParser());

        FoxHttpRequestHeader foxHttpHeader = new FoxHttpRequestHeader();
        foxHttpHeader.addHeader("Name", "FoxHttp");

        FoxHttpRequest foxHttpRequest = new FoxHttpRequest(foxHttpClient);
        foxHttpRequest.setUrl(new URL(endpoint + "get"));
        foxHttpRequest.setRequestType(RequestType.GET);
        foxHttpRequest.setRequestHeader(foxHttpHeader);
        foxHttpRequest.setFollowRedirect(true);

        FoxHttpResponse foxHttpResponse = foxHttpRequest.execute();

        assertThat(foxHttpResponse.getResponseCode()).isEqualTo(200);
        assertThat(foxHttpResponse.getByteArrayOutputStreamBody().size()).isGreaterThan(0);

        GetResponse getResponse = foxHttpResponse.getParsedBody(GetResponse.class);

        assertThat(getResponse.getUrl()).isEqualTo(endpoint + "get");
        assertThat(getResponse.getHeaders().get("Name")).isEqualTo("FoxHttp");
    }

    @Test
    public void getRequestBodyException() throws Exception {

        FoxHttpRequest foxHttpRequest = new FoxHttpRequest();
        foxHttpRequest.setUrl(new URL(endpoint + "get"));
        foxHttpRequest.setRequestType(RequestType.GET);
        foxHttpRequest.setFollowRedirect(true);

        //Exception part
        foxHttpRequest.setRequestBody(new RequestStringBody("EXCEPTION"));

        try {
            FoxHttpResponse foxHttpResponse = foxHttpRequest.execute();
            assertThat(false).isEqualTo(true);
        } catch (FoxHttpRequestException e) {
            assertThat(e.getMessage()).isEqualTo("Request type 'GET' does not allow a request body!");
        }


    }

    @Test
    public void postRequest() throws Exception {

        FoxHttpClient foxHttpClient = new FoxHttpClient();
        foxHttpClient.setFoxHttpResponseParser(new GsonParser());

        FoxHttpRequestBody requestBody = new RequestStringBody("test string body 1234 - ?");

        FoxHttpRequest foxHttpRequest = new FoxHttpRequest(foxHttpClient);
        foxHttpRequest.setUrl(new URL(endpoint + "post"));
        foxHttpRequest.setRequestType(RequestType.POST);
        foxHttpRequest.setFollowRedirect(true);
        foxHttpRequest.setRequestBody(requestBody);

        FoxHttpResponse foxHttpResponse = foxHttpRequest.execute();

        assertThat(foxHttpResponse.getResponseCode()).isEqualTo(200);
        assertThat(foxHttpResponse.getByteArrayOutputStreamBody().size()).isGreaterThan(0);

        PostResponse postResponse = foxHttpResponse.getParsedBody(PostResponse.class);

        assertThat(postResponse.getData()).isEqualTo("test string body 1234 - ?");
    }

    @Test
    public void proxyRequest() throws Exception {

        FoxHttpProxyStrategy foxHttpProxyStrategy = new FoxHttpProxyStrategy() {
            @Override
            public Proxy getProxy(URL url) {
                return new Proxy(Proxy.Type.HTTP, new InetSocketAddress("httpbin.org", 80));
            }

            @Override
            public String getProxyAuthorization(URL url) {
                return null;
            }

            @Override
            public boolean hasProxyAuthorization(URL url) {
                return false;
            }
        };


        FoxHttpClient foxHttpClient = new FoxHttpClient();
        foxHttpClient.setFoxHttpResponseParser(new GsonParser());
        foxHttpClient.setFoxHttpProxyStrategy(foxHttpProxyStrategy);


        FoxHttpRequestBody requestBody = new RequestStringBody("test string body 1234 - ?");

        FoxHttpRequest foxHttpRequest = new FoxHttpRequest(foxHttpClient);
        foxHttpRequest.setUrl(new URL(endpoint + "post"));
        foxHttpRequest.setRequestType(RequestType.POST);
        foxHttpRequest.setFollowRedirect(true);
        foxHttpRequest.setRequestBody(requestBody);

        FoxHttpResponse foxHttpResponse = foxHttpRequest.execute();

        assertThat(foxHttpResponse.getResponseCode()).isEqualTo(200);
        assertThat(foxHttpResponse.getByteArrayOutputStreamBody().size()).isGreaterThan(0);

        PostResponse postResponse = foxHttpResponse.getParsedBody(PostResponse.class);

        assertThat(postResponse.getData()).isEqualTo("test string body 1234 - ?");
    }

    @Test
    public void basicAuthRequest() throws Exception {
        FoxHttpClient foxHttpClient = new FoxHttpClient();
        foxHttpClient.setFoxHttpResponseParser(new GsonParser());

        foxHttpClient.getFoxHttpAuthorizationStrategy().addAuthorization(
                FoxHttpAuthorizationScope.create(endpoint + "*basic-auth/*", RequestType.GET),
                new BasicAuthAuthorization("FoxHttp", "GroundWork123")
        );

        FoxHttpRequest foxHttpRequest = new FoxHttpRequest(foxHttpClient);
        foxHttpRequest.setUrl(new URL(endpoint + "basic-auth/FoxHttp/GroundWork123"));
        foxHttpRequest.setRequestType(RequestType.GET);
        foxHttpRequest.setFollowRedirect(true);

        FoxHttpResponse foxHttpResponse = foxHttpRequest.execute();

        assertThat(foxHttpResponse.getResponseCode()).isEqualTo(200);
        assertThat(foxHttpResponse.getByteArrayOutputStreamBody().size()).isGreaterThan(0);

        BasicAuthResponse basicAuthResponse = foxHttpResponse.getParsedBody(BasicAuthResponse.class);

        assertThat(basicAuthResponse.getUser()).isEqualTo("FoxHttp");
    }

    @Test
    public void bearerTokenRequest() throws Exception {
        FoxHttpClient foxHttpClient = new FoxHttpClient();
        foxHttpClient.setFoxHttpResponseParser(new GsonParser());

        foxHttpClient.getFoxHttpAuthorizationStrategy().addAuthorization(
                FoxHttpAuthorizationScope.ANY,
                new BearerTokenAuthorization("FoxHttp-GroundWork-Token-1234567890")
        );

        FoxHttpRequest foxHttpRequest = new FoxHttpRequest(foxHttpClient);
        foxHttpRequest.setUrl(new URL(endpoint + "get"));
        foxHttpRequest.setRequestType(RequestType.GET);
        foxHttpRequest.setFollowRedirect(true);

        FoxHttpResponse foxHttpResponse = foxHttpRequest.execute();

        assertThat(foxHttpResponse.getResponseCode()).isEqualTo(200);
        assertThat(foxHttpResponse.getByteArrayOutputStreamBody().size()).isGreaterThan(0);

        GetResponse getResponse = foxHttpResponse.getParsedBody(GetResponse.class);

        assertThat(getResponse.getHeaders().get("Authorization")).isEqualTo("Bearer FoxHttp-GroundWork-Token-1234567890");
    }

    @Test
    public void multiAuthRequest() throws Exception {
        FoxHttpClient foxHttpClient = new FoxHttpClient();
        foxHttpClient.setFoxHttpResponseParser(new GsonParser());

        foxHttpClient.getFoxHttpAuthorizationStrategy().addAuthorization(
                FoxHttpAuthorizationScope.ANY,
                new BearerTokenAuthorization("FoxHttp-GroundWork-Token-1234567890")
        );

        foxHttpClient.getFoxHttpAuthorizationStrategy().addAuthorization(
                FoxHttpAuthorizationScope.ANY,
                (connection, foxHttpAuthorizationScope) -> connection.setRequestProperty("Product-Key", "GroundWork FoxHttp")
        );

        foxHttpClient.getFoxHttpAuthorizationStrategy().addAuthorization(
                FoxHttpAuthorizationScope.ANY,
                new RemoveMeAuthorization()
        );

        foxHttpClient.getFoxHttpAuthorizationStrategy().removeAuthorization(FoxHttpAuthorizationScope.ANY, new RemoveMeAuthorization());

        FoxHttpRequest foxHttpRequest = new FoxHttpRequest(foxHttpClient);
        foxHttpRequest.setUrl(new URL(endpoint + "get"));
        foxHttpRequest.setRequestType(RequestType.GET);
        foxHttpRequest.setFollowRedirect(true);

        FoxHttpResponse foxHttpResponse = foxHttpRequest.execute();

        assertThat(foxHttpResponse.getResponseCode()).isEqualTo(200);
        assertThat(foxHttpResponse.getByteArrayOutputStreamBody().size()).isGreaterThan(0);

        GetResponse getResponse = foxHttpResponse.getParsedBody(GetResponse.class);

        assertThat(getResponse.getHeaders().get("Authorization")).isEqualTo("Bearer FoxHttp-GroundWork-Token-1234567890");
        assertThat(getResponse.getHeaders().get("Product-Key")).isEqualTo("GroundWork FoxHttp");

        assertThat(getResponse.getHeaders().get("Remove-Me")).isNull();

    }

    @Test
    public void cookieTest() throws Exception {
        FoxHttpRequestQuery foxHttpQuery = new FoxHttpRequestQuery();
        foxHttpQuery.addQueryEntry("k1", "v1");
        foxHttpQuery.addQueryEntry("k2", "v2");

        FoxHttpClient foxHttpClient = new FoxHttpClientBuilder(new GsonParser(), new GsonParser()).build();

        FoxHttpRequest foxHttpRequest = new FoxHttpRequestBuilder("http://httpbin.org/cookies/set", RequestType.GET, foxHttpClient)
                .setRequestQuery(foxHttpQuery).build();

        FoxHttpResponse foxHttpResponse = foxHttpRequest.execute();

        CookieResponse cookieResponse = foxHttpResponse.getParsedBody(CookieResponse.class);

        assertThat(cookieResponse.getCookies()).containsKey("k1");
        assertThat(cookieResponse.getCookies()).containsKey("k2");

        List<HttpCookie> cookies = ((DefaultCookieStore) foxHttpClient.getFoxHttpCookieStore()).getCookieStore().getCookies();

        assertThat(cookies.get(0).getValue()).isEqualTo("v1");
        assertThat(cookies.get(1).getValue()).isEqualTo("v2");
    }

    @Test
    public void getSSLRequest() throws Exception {
//TODO: Test with invalide SSL
        FoxHttpClient foxHttpClient = new FoxHttpClient();
        foxHttpClient.setFoxHttpResponseParser(new GsonParser());

        FoxHttpRequest foxHttpRequest = new FoxHttpRequest(foxHttpClient);
        foxHttpRequest.setUrl(new URL(sslEndpoint + "get"));
        foxHttpRequest.setRequestType(RequestType.GET);
        foxHttpRequest.setFollowRedirect(true);

        FoxHttpResponse foxHttpResponse = foxHttpRequest.execute();

        assertThat(foxHttpResponse.getResponseCode()).isEqualTo(200);
        assertThat(foxHttpResponse.getByteArrayOutputStreamBody().size()).isGreaterThan(0);
        assertThat(foxHttpResponse.getFoxHttpRequest().getRequestType()).isEqualTo(RequestType.GET);
        assertThat(foxHttpResponse.getFoxHttpRequest().getUrl()).isEqualTo(new URL(sslEndpoint + "get"));

        GetResponse getResponse = foxHttpResponse.getParsedBody(GetResponse.class);

        assertThat(getResponse.getUrl()).isEqualTo(sslEndpoint + "get");
    }

    @Test
    public void systemOutLoggerTest() {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        PrintStream old = System.out;

        System.setOut(ps);

        SystemOutFoxHttpLogger logger = new SystemOutFoxHttpLogger(true, "TEST-CASE");
        logger.log("Test 1");
        logger.log("Test 2", "TEST-CASE-Override");
        logger.log("Test3");

        System.out.flush();
        System.setOut(old);

        assertThat(baos.toString()).contains("TEST-CASE: Test 1");
        assertThat(baos.toString()).contains("TEST-CASE-Override: Test 2");
        assertThat(baos.toString()).contains("TEST-CASE: Test3");
    }


    @Test
    public void placeholderTest() throws Exception {

        FoxHttpRequestBuilder builder = new FoxHttpRequestBuilder("{endpoint}get", RequestType.GET);
        builder.addFoxHttpPlaceholderEntry("endpoint", endpoint);

        FoxHttpRequest request = builder.build();

        assertThat(request.getUrl().toString()).isEqualTo(endpoint + "get");
    }
}
