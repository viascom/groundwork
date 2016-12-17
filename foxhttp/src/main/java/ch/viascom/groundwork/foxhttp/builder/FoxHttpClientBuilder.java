package ch.viascom.groundwork.foxhttp.builder;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorization;
import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationScope;
import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationStrategy;
import ch.viascom.groundwork.foxhttp.cookie.FoxHttpCookieStore;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptorType;
import ch.viascom.groundwork.foxhttp.interceptor.response.DeflateResponseInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.response.GZipResponseInterceptor;
import ch.viascom.groundwork.foxhttp.log.FoxHttpLogger;
import ch.viascom.groundwork.foxhttp.parser.FoxHttpParser;
import ch.viascom.groundwork.foxhttp.proxy.FoxHttpProxyStrategy;
import ch.viascom.groundwork.foxhttp.ssl.FoxHttpHostTrustStrategy;
import ch.viascom.groundwork.foxhttp.ssl.FoxHttpSSLTrustStrategy;
import ch.viascom.groundwork.foxhttp.timeout.FoxHttpTimeoutStrategy;
import ch.viascom.groundwork.foxhttp.timeout.UserDefinedTimeoutStrategy;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpClientBuilder {

    private FoxHttpClient foxHttpClient;

    // -- Constructors
    public FoxHttpClientBuilder() {
        foxHttpClient = new FoxHttpClient();
    }

    public FoxHttpClientBuilder(FoxHttpParser foxHttpResponseParser, FoxHttpParser foxHttpRequestParser) {
        foxHttpClient = new FoxHttpClient();
        foxHttpClient.setFoxHttpRequestParser(foxHttpRequestParser);
        foxHttpClient.setFoxHttpResponseParser(foxHttpResponseParser);
    }

    public FoxHttpClientBuilder(FoxHttpAuthorizationStrategy foxHttpAuthorizationStrategy) {
        foxHttpClient = new FoxHttpClient();
        foxHttpClient.setFoxHttpAuthorizationStrategy(foxHttpAuthorizationStrategy);
    }

    public FoxHttpClientBuilder(FoxHttpHostTrustStrategy foxHttpHostTrustStrategy, FoxHttpSSLTrustStrategy foxHttpSSLTrustStrategy) {
        foxHttpClient = new FoxHttpClient();
        foxHttpClient.setFoxHttpHostTrustStrategy(foxHttpHostTrustStrategy);
        foxHttpClient.setFoxHttpSSLTrustStrategy(foxHttpSSLTrustStrategy);
    }


    // -- Setters

    public FoxHttpClientBuilder setFoxHttpResponseParser(FoxHttpParser foxHttpResponseParser) {
        foxHttpClient.setFoxHttpResponseParser(foxHttpResponseParser);
        return this;
    }

    public FoxHttpClientBuilder setFoxHttpRequestParser(FoxHttpParser foxHttpRequestParser) {
        foxHttpClient.setFoxHttpRequestParser(foxHttpRequestParser);
        return this;
    }

    public FoxHttpClientBuilder setFoxHttpInterceptors(HashMap<FoxHttpInterceptorType, ArrayList<FoxHttpInterceptor>> foxHttpInterceptors) {
        foxHttpClient.setFoxHttpInterceptors(foxHttpInterceptors);
        return this;
    }

    public FoxHttpClientBuilder registerFoxHttpInterceptor(FoxHttpInterceptorType interceptorType, FoxHttpInterceptor foxHttpInterceptor) throws FoxHttpException {
        foxHttpClient.register(interceptorType, foxHttpInterceptor);
        return this;
    }

    public FoxHttpClientBuilder activteGZipResponseInterceptor() throws FoxHttpException {
        foxHttpClient.register(FoxHttpInterceptorType.RESPONSE, new GZipResponseInterceptor());
        return this;
    }

    public FoxHttpClientBuilder activteGZipResponseInterceptor(int weight) throws FoxHttpException {
        foxHttpClient.register(FoxHttpInterceptorType.RESPONSE, new GZipResponseInterceptor(weight));
        return this;
    }

    /**
     * @param nowrap if true then support GZIP compatible compression
     * @return
     * @throws FoxHttpException
     */
    public FoxHttpClientBuilder activteDeflateResponseInterceptor(boolean nowrap) throws FoxHttpException {
        foxHttpClient.register(FoxHttpInterceptorType.RESPONSE, new DeflateResponseInterceptor(nowrap));
        return this;

    }

    /**
     * @param nowrap if true then support GZIP compatible compression
     * @return
     * @throws FoxHttpException
     */
    public FoxHttpClientBuilder activteDeflateResponseInterceptor(boolean nowrap, int weight) throws FoxHttpException {
        foxHttpClient.register(FoxHttpInterceptorType.RESPONSE, new DeflateResponseInterceptor(nowrap, weight));
        return this;

    }

    public FoxHttpClientBuilder setFoxHttpCookieStore(FoxHttpCookieStore foxHttpCookieStore) {
        foxHttpClient.setFoxHttpCookieStore(foxHttpCookieStore);
        return this;
    }

    public FoxHttpClientBuilder setFoxHttpAuthorizationStrategy(FoxHttpAuthorizationStrategy foxHttpAuthorizationStrategy) {
        foxHttpClient.setFoxHttpAuthorizationStrategy(foxHttpAuthorizationStrategy);
        return this;
    }

    public FoxHttpClientBuilder addFoxHttpAuthorization(FoxHttpAuthorizationScope foxHttpAuthorizationScope, FoxHttpAuthorization foxHttpAuthorization) {
        foxHttpClient.getFoxHttpAuthorizationStrategy().addAuthorization(foxHttpAuthorizationScope, foxHttpAuthorization);
        return this;
    }

    public FoxHttpClientBuilder setFoxHttpTimeoutStrategy(FoxHttpTimeoutStrategy foxHttpTimeoutStrategy) {
        foxHttpClient.setFoxHttpTimeoutStrategy(foxHttpTimeoutStrategy);
        return this;
    }

    public FoxHttpClientBuilder setFoxHttpTimeouts(int connectionTimeout, int readTimeout) {
        foxHttpClient.setFoxHttpTimeoutStrategy(new UserDefinedTimeoutStrategy(connectionTimeout, readTimeout));
        return this;
    }

    public FoxHttpClientBuilder setFoxHttpHostTrustStrategy(FoxHttpHostTrustStrategy foxHttpHostTrustStrategy) {
        foxHttpClient.setFoxHttpHostTrustStrategy(foxHttpHostTrustStrategy);
        return this;
    }

    public FoxHttpClientBuilder setFoxHttpSSLTrustStrategy(FoxHttpSSLTrustStrategy foxHttpSSLTrustStrategy) {
        foxHttpClient.setFoxHttpSSLTrustStrategy(foxHttpSSLTrustStrategy);
        return this;
    }

    public FoxHttpClientBuilder setFoxHttpProxyStrategy(FoxHttpProxyStrategy foxHttpProxyStrategy) {
        foxHttpClient.setFoxHttpProxyStrategy(foxHttpProxyStrategy);
        return this;
    }

    public FoxHttpClientBuilder setFoxHttpLogger(FoxHttpLogger foxHttpLogger) {
        foxHttpClient.setFoxHttpLogger(foxHttpLogger);
        return this;
    }

    public FoxHttpClientBuilder activteFoxHttpDefaultLogger(boolean activate) {
        foxHttpClient.getFoxHttpLogger().setLoggingEnabled(activate);
        return this;
    }

    public FoxHttpClientBuilder setFoxHttpUserAgent(String foxHttpUserAgent) {
        foxHttpClient.setFoxHttpUserAgent(foxHttpUserAgent);
        return this;
    }

    public FoxHttpClient build() {
        return this.foxHttpClient;
    }
}
