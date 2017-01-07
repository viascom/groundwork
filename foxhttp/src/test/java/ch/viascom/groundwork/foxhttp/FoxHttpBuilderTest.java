package ch.viascom.groundwork.foxhttp;

import ch.viascom.groundwork.foxhttp.authorization.*;
import ch.viascom.groundwork.foxhttp.body.request.RequestStringBody;
import ch.viascom.groundwork.foxhttp.builder.FoxHttpClientBuilder;
import ch.viascom.groundwork.foxhttp.builder.FoxHttpRequestBuilder;
import ch.viascom.groundwork.foxhttp.cookie.DefaultCookieStore;
import ch.viascom.groundwork.foxhttp.cookie.FoxHttpCookieStore;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.header.HeaderEntry;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptorType;
import ch.viascom.groundwork.foxhttp.interceptor.request.FoxHttpRequestBodyInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestBodyInterceptorContext;
import ch.viascom.groundwork.foxhttp.log.DefaultFoxHttpLogger;
import ch.viascom.groundwork.foxhttp.log.FoxHttpLogger;
import ch.viascom.groundwork.foxhttp.parser.FoxHttpParser;
import ch.viascom.groundwork.foxhttp.parser.GsonParser;
import ch.viascom.groundwork.foxhttp.placeholder.FoxHttpPlaceholderStrategy;
import ch.viascom.groundwork.foxhttp.proxy.FoxHttpProxyStrategy;
import ch.viascom.groundwork.foxhttp.ssl.DefaultHostTrustStrategy;
import ch.viascom.groundwork.foxhttp.ssl.DefaultSSLTrustStrategy;
import ch.viascom.groundwork.foxhttp.ssl.FoxHttpHostTrustStrategy;
import ch.viascom.groundwork.foxhttp.ssl.FoxHttpSSLTrustStrategy;
import ch.viascom.groundwork.foxhttp.timeout.DefaultTimeoutStrategy;
import ch.viascom.groundwork.foxhttp.timeout.FoxHttpTimeoutStrategy;
import ch.viascom.groundwork.foxhttp.type.ContentType;
import ch.viascom.groundwork.foxhttp.type.HeaderTypes;
import ch.viascom.groundwork.foxhttp.type.RequestType;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.EnumMap;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpBuilderTest {

    @Test
    public void clientBuilderTest() throws Exception {
        FoxHttpClientBuilder foxHttpClientBuilder = new FoxHttpClientBuilder();

        FoxHttpHostTrustStrategy foxHttpHostTrustStrategy = new DefaultHostTrustStrategy();
        FoxHttpAuthorizationStrategy foxHttpAuthorizationStrategy = new DefaultAuthorizationStrategy();
        FoxHttpParser foxHttpParser = new GsonParser();
        FoxHttpLogger foxHttpLogger = new DefaultFoxHttpLogger(false);
        FoxHttpCookieStore foxHttpCookieStore = new DefaultCookieStore();
        FoxHttpProxyStrategy foxHttpProxyStrategy = new FoxHttpProxyStrategy() {
            @Override
            public Proxy getProxy(URL url) {
                return null;
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
        FoxHttpSSLTrustStrategy foxHttpSSLTrustStrategy = new DefaultSSLTrustStrategy();
        FoxHttpTimeoutStrategy foxHttpTimeoutStrategy = new DefaultTimeoutStrategy();
        FoxHttpAuthorization authorization = new BasicAuthAuthorization("name", "passwd");
        FoxHttpPlaceholderStrategy placeholderStrategy = new FoxHttpPlaceholderStrategy() {
            @Override
            public String getPlaceholderEscapeCharStart() {
                return "[";
            }

            @Override
            public void setPlaceholderEscapeCharStart(String placeholderEscapeChar) {

            }

            @Override
            public String getPlaceholderEscapeCharEnd() {
                return null;
            }

            @Override
            public void setPlaceholderEscapeCharEnd(String placeholderEscapeChar) {

            }

            @Override
            public String getPlaceholderMatchRegex() {
                return null;
            }

            @Override
            public void addPlaceholder(String placeholder, String value) {

            }

            @Override
            public Map<String, String> getPlaceholderMap() {
                return null;
            }
        };

        foxHttpClientBuilder.setFoxHttpHostTrustStrategy(foxHttpHostTrustStrategy);
        foxHttpClientBuilder.setFoxHttpResponseParser(foxHttpParser);
        foxHttpClientBuilder.setFoxHttpLogger(foxHttpLogger);
        foxHttpClientBuilder.setFoxHttpRequestParser(foxHttpParser);
        foxHttpClientBuilder.setFoxHttpAuthorizationStrategy(foxHttpAuthorizationStrategy);
        foxHttpClientBuilder.setFoxHttpCookieStore(foxHttpCookieStore);
        foxHttpClientBuilder.setFoxHttpInterceptors(new EnumMap<>(FoxHttpInterceptorType.class));
        foxHttpClientBuilder.setFoxHttpProxyStrategy(foxHttpProxyStrategy);
        foxHttpClientBuilder.setFoxHttpSSLTrustStrategy(foxHttpSSLTrustStrategy);
        foxHttpClientBuilder.setFoxHttpTimeouts(0, 0);
        foxHttpClientBuilder.setFoxHttpUserAgent("FoxHttp v1.0");
        foxHttpClientBuilder.addFoxHttpAuthorization(FoxHttpAuthorizationScope.ANY, authorization);
        foxHttpClientBuilder.activteGZipResponseInterceptor(100);
        foxHttpClientBuilder.registerFoxHttpInterceptor(FoxHttpInterceptorType.REQUEST_BODY, new FoxHttpRequestBodyInterceptor() {
            @Override
            public void onIntercept(FoxHttpRequestBodyInterceptorContext context) throws FoxHttpException {
                System.out.println(context);
            }

            @Override
            public int getWeight() {
                return 0;
            }
        });
        foxHttpClientBuilder.setFoxHttpPlaceholderStrategy(placeholderStrategy);

        FoxHttpClient foxHttpClient = foxHttpClientBuilder.build();

        assertThat(foxHttpClient.getFoxHttpAuthorizationStrategy()).isEqualTo(foxHttpAuthorizationStrategy);
        assertThat(foxHttpClient.getFoxHttpHostTrustStrategy()).isEqualTo(foxHttpHostTrustStrategy);
        assertThat(foxHttpClient.getFoxHttpRequestParser()).isEqualTo(foxHttpParser);
        assertThat(foxHttpClient.getFoxHttpResponseParser()).isEqualTo(foxHttpParser);
        assertThat(foxHttpClient.getFoxHttpLogger()).isEqualTo(foxHttpLogger);
        assertThat(foxHttpClient.getFoxHttpCookieStore()).isEqualTo(foxHttpCookieStore);
        assertThat(foxHttpClient.getFoxHttpProxyStrategy()).isEqualTo(foxHttpProxyStrategy);
        assertThat(foxHttpClient.getFoxHttpSSLTrustStrategy()).isEqualTo(foxHttpSSLTrustStrategy);
        assertThat(foxHttpClient.getFoxHttpUserAgent()).isEqualTo("FoxHttp v1.0");
        assertThat(foxHttpClient.getFoxHttpTimeoutStrategy().getConnectionTimeout()).isEqualTo(0);
        assertThat(foxHttpClient.getFoxHttpTimeoutStrategy().getReadTimeout()).isEqualTo(0);
        assertThat(foxHttpClient.getFoxHttpAuthorizationStrategy().getAuthorization(null, FoxHttpAuthorizationScope.ANY).get(0)).isEqualTo(authorization);
        assertThat(foxHttpClient.getFoxHttpInterceptors().get(FoxHttpInterceptorType.RESPONSE)).isNotEmpty();
        assertThat(foxHttpClient.getFoxHttpInterceptors().get(FoxHttpInterceptorType.RESPONSE).get(0).getWeight()).isEqualTo(100);
        assertThat(foxHttpClient.getFoxHttpPlaceholderStrategy().getPlaceholderEscapeCharStart()).isEqualTo("[");

        foxHttpClientBuilder.activteFoxHttpLogger(true);
        foxHttpClientBuilder.setFoxHttpTimeoutStrategy(foxHttpTimeoutStrategy);
        foxHttpClientBuilder.setFoxHttpInterceptors(new EnumMap<>(FoxHttpInterceptorType.class));
        foxHttpClientBuilder.activteDeflateResponseInterceptor(true, 100);

        FoxHttpClient foxHttpClient2 = foxHttpClientBuilder.build();

        assertThat(foxHttpClient2.getFoxHttpTimeoutStrategy().getConnectionTimeout()).isEqualTo(0);
        assertThat(foxHttpClient2.getFoxHttpInterceptors().get(FoxHttpInterceptorType.RESPONSE)).isNotEmpty();
        assertThat(foxHttpClient2.getFoxHttpInterceptors().get(FoxHttpInterceptorType.RESPONSE).get(0).getWeight()).isEqualTo(100);

        foxHttpClientBuilder.setFoxHttpInterceptors(new EnumMap<>(FoxHttpInterceptorType.class));
        foxHttpClientBuilder.activteDeflateResponseInterceptor(true);
        foxHttpClientBuilder.activteGZipResponseInterceptor();

        FoxHttpClient foxHttpClient3 = foxHttpClientBuilder.build();
        assertThat(foxHttpClient3.getFoxHttpTimeoutStrategy().getConnectionTimeout()).isEqualTo(0);
        assertThat(foxHttpClient3.getFoxHttpInterceptors().get(FoxHttpInterceptorType.RESPONSE)).isNotEmpty();
        assertThat(foxHttpClient3.getFoxHttpInterceptors().get(FoxHttpInterceptorType.RESPONSE).size()).isEqualTo(2);
    }

    @Test
    public void clientInterceptorBuilderTest() throws Exception {
        FoxHttpClientBuilder foxHttpClientBuilder = new FoxHttpClientBuilder(new GsonParser(), new GsonParser());

        FoxHttpInterceptor foxHttpInterceptor = new FoxHttpRequestBodyInterceptor() {
            @Override
            public void onIntercept(FoxHttpRequestBodyInterceptorContext context) throws FoxHttpException {
                try {
                    context.getRequest().setUrl(new URL("TEST"));
                } catch (MalformedURLException e) {
                    throw new FoxHttpException(e);
                }
            }

            @Override
            public int getWeight() {
                return 0;
            }
        };

        foxHttpClientBuilder.registerFoxHttpInterceptor(FoxHttpInterceptorType.REQUEST_BODY, foxHttpInterceptor);

        FoxHttpClient foxHttpClient = foxHttpClientBuilder.build();

        assertThat(foxHttpClient.getFoxHttpInterceptors().get(FoxHttpInterceptorType.REQUEST_BODY).get(0)).isEqualTo(foxHttpInterceptor);

    }

    @Test
    public void requestBuilderTest() throws Exception {

        FoxHttpInterceptor foxHttpInterceptor = new FoxHttpRequestBodyInterceptor() {
            @Override
            public void onIntercept(FoxHttpRequestBodyInterceptorContext context) throws FoxHttpException {

            }

            @Override
            public int getWeight() {
                return 0;
            }
        };

        HeaderEntry headerField = new HeaderEntry("Product", "GroundWork");

        FoxHttpRequestBuilder requestBuilder = new FoxHttpRequestBuilder("http://httpbin.org/{method}");
        requestBuilder.setRequestType(RequestType.POST);
        requestBuilder.setFollowRedirect(true);
        requestBuilder.addRequestHeader("Fox-Header", "true");
        requestBuilder.addRequestHeader(HeaderTypes.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        requestBuilder.addRequestHeader(headerField);
        requestBuilder.addRequestQueryEntry("name", "FoxHttp");
        requestBuilder.setSkipResponseBody(false);
        requestBuilder.registerFoxHttpInterceptor(FoxHttpInterceptorType.REQUEST_BODY, foxHttpInterceptor);
        requestBuilder.setRequestBody(new RequestStringBody("Hi!"));
        requestBuilder.addFoxHttpPlaceholderEntry("method","post");

        FoxHttpRequest foxHttpRequest = requestBuilder.build();

        assertThat(foxHttpRequest.getRequestType()).isEqualTo(RequestType.POST);
        assertThat(foxHttpRequest.getUrl().toString()).isEqualTo("http://httpbin.org/post");
        assertThat(foxHttpRequest.getFoxHttpClient().getFoxHttpPlaceholderStrategy().getPlaceholderMap().get("method")).isEqualTo("post");

    }

    @Test
    public void requestConstructorBuilderTest() throws Exception {

        FoxHttpRequestBuilder requestBuilder = new FoxHttpRequestBuilder();
        FoxHttpRequest foxHttpRequest = requestBuilder.build();
        assertThat(foxHttpRequest.getFoxHttpClient()).isNotNull();

        FoxHttpRequestBuilder requestBuilder2 = new FoxHttpRequestBuilder("http://httpbin.org/post", RequestType.DELETE);
        FoxHttpRequest foxHttpRequest2 = requestBuilder2.build();
        assertThat(foxHttpRequest2.getFoxHttpClient()).isNotNull();
        assertThat(foxHttpRequest2.getRequestType()).isEqualTo(RequestType.DELETE);
        assertThat(foxHttpRequest2.getUrl().toString()).isEqualTo("http://httpbin.org/post");

        FoxHttpClient foxHttpClient = new FoxHttpClient();
        FoxHttpRequestBuilder requestBuilder3 = new FoxHttpRequestBuilder("http://httpbin.org/put", RequestType.PUT, foxHttpClient);
        FoxHttpRequest foxHttpRequest3 = requestBuilder3.build();
        assertThat(foxHttpRequest3.getFoxHttpClient()).isNotNull();
        assertThat(foxHttpRequest3.getRequestType()).isEqualTo(RequestType.PUT);
        assertThat(foxHttpRequest3.getUrl().toString()).isEqualTo("http://httpbin.org/put");
        assertThat(foxHttpRequest3.getFoxHttpClient()).isEqualTo(foxHttpClient);
    }

}
