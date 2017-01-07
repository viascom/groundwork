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
import ch.viascom.groundwork.foxhttp.placeholder.FoxHttpPlaceholderStrategy;
import ch.viascom.groundwork.foxhttp.proxy.FoxHttpProxyStrategy;
import ch.viascom.groundwork.foxhttp.ssl.FoxHttpHostTrustStrategy;
import ch.viascom.groundwork.foxhttp.ssl.FoxHttpSSLTrustStrategy;
import ch.viascom.groundwork.foxhttp.timeout.FoxHttpTimeoutStrategy;
import ch.viascom.groundwork.foxhttp.timeout.UserDefinedTimeoutStrategy;

import java.util.ArrayList;
import java.util.Map;

/**
 * FoxHttpClient builder to create a new FoxHttpClient
 *
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpClientBuilder {

    private FoxHttpClient foxHttpClient;

    // -- Constructors

    /**
     * Create a new builder with a default FoxHttpClient
     */
    public FoxHttpClientBuilder() {
        foxHttpClient = new FoxHttpClient();
    }

    /**
     * Create a new builder with a default FoxHttpClient except for the response and request parser
     *
     * @param foxHttpResponseParser a response parser
     * @param foxHttpRequestParser  a request parser
     */
    public FoxHttpClientBuilder(FoxHttpParser foxHttpResponseParser, FoxHttpParser foxHttpRequestParser) {
        foxHttpClient = new FoxHttpClient();
        foxHttpClient.setFoxHttpRequestParser(foxHttpRequestParser);
        foxHttpClient.setFoxHttpResponseParser(foxHttpResponseParser);
    }

    /**
     * Create a new builder with a default FoxHttpClient except for the authorization strategy
     *
     * @param foxHttpAuthorizationStrategy an authorization strategy
     */
    public FoxHttpClientBuilder(FoxHttpAuthorizationStrategy foxHttpAuthorizationStrategy) {
        foxHttpClient = new FoxHttpClient();
        foxHttpClient.setFoxHttpAuthorizationStrategy(foxHttpAuthorizationStrategy);
    }

    /**
     * Create a new builder with a default FoxHttpClient except for the host and ssl trust strategy
     *
     * @param foxHttpHostTrustStrategy a host trust strategy
     * @param foxHttpSSLTrustStrategy  a ssl trust strategy
     */
    public FoxHttpClientBuilder(FoxHttpHostTrustStrategy foxHttpHostTrustStrategy, FoxHttpSSLTrustStrategy foxHttpSSLTrustStrategy) {
        foxHttpClient = new FoxHttpClient();
        foxHttpClient.setFoxHttpHostTrustStrategy(foxHttpHostTrustStrategy);
        foxHttpClient.setFoxHttpSSLTrustStrategy(foxHttpSSLTrustStrategy);
    }


    // -- Setters

    /**
     * Set the response parser
     *
     * @param foxHttpResponseParser A FoxHttpParser
     * @return FoxHttpClientBuilder (this)
     */
    public FoxHttpClientBuilder setFoxHttpResponseParser(FoxHttpParser foxHttpResponseParser) {
        foxHttpClient.setFoxHttpResponseParser(foxHttpResponseParser);
        return this;
    }

    /**
     * Set the request parser
     *
     * @param foxHttpRequestParser A FoxHttpParser
     * @return FoxHttpClientBuilder (this)
     */
    public FoxHttpClientBuilder setFoxHttpRequestParser(FoxHttpParser foxHttpRequestParser) {
        foxHttpClient.setFoxHttpRequestParser(foxHttpRequestParser);
        return this;
    }

    /**
     * Define a map of FoxHttpInterceptors
     * <i>This will override the existing map of interceptors</i>
     *
     * @param foxHttpInterceptors Map of interceptors
     * @return FoxHttpClientBuilder (this)
     */
    public FoxHttpClientBuilder setFoxHttpInterceptors(Map<FoxHttpInterceptorType, ArrayList<FoxHttpInterceptor>> foxHttpInterceptors) {
        foxHttpClient.setFoxHttpInterceptors(foxHttpInterceptors);
        return this;
    }

    /**
     * Register an interceptor
     *
     * @param interceptorType    Type of the interceptor
     * @param foxHttpInterceptor Interceptor instance
     * @return FoxHttpClientBuilder (this)
     * @throws FoxHttpException Throws an exception if the interceptor does not match the type
     */
    public FoxHttpClientBuilder registerFoxHttpInterceptor(FoxHttpInterceptorType interceptorType, FoxHttpInterceptor foxHttpInterceptor) throws FoxHttpException {
        foxHttpClient.register(interceptorType, foxHttpInterceptor);
        return this;
    }

    /**
     * Automatic gzip response parser.
     *
     * @return FoxHttpClientBuilder (this)
     * @throws FoxHttpException Throws an exception if the interceptor does not match the type
     */
    public FoxHttpClientBuilder activteGZipResponseInterceptor() throws FoxHttpException {
        foxHttpClient.register(FoxHttpInterceptorType.RESPONSE, new GZipResponseInterceptor());
        return this;
    }

    /**
     * Automatic gzip response parser.
     *
     * @param weight weight of the interceptor
     * @return FoxHttpClientBuilder (this)
     * @throws FoxHttpException Throws an exception if the interceptor does not match the type
     */
    public FoxHttpClientBuilder activteGZipResponseInterceptor(int weight) throws FoxHttpException {
        foxHttpClient.register(FoxHttpInterceptorType.RESPONSE, new GZipResponseInterceptor(weight));
        return this;
    }

    /**
     * @param nowrap if true then support GZIP compatible compression
     * @return FoxHttpClientBuilder (this)
     * @throws FoxHttpException Throws an exception if the interceptor does not match the type
     */
    public FoxHttpClientBuilder activteDeflateResponseInterceptor(boolean nowrap) throws FoxHttpException {
        foxHttpClient.register(FoxHttpInterceptorType.RESPONSE, new DeflateResponseInterceptor(nowrap));
        return this;

    }

    /**
     * @param nowrap if true then support GZIP compatible compression
     * @param weight weight of the interceptor
     * @return FoxHttpClientBuilder (this)
     * @throws FoxHttpException Throws an exception if the interceptor does not match the type
     */
    public FoxHttpClientBuilder activteDeflateResponseInterceptor(boolean nowrap, int weight) throws FoxHttpException {
        foxHttpClient.register(FoxHttpInterceptorType.RESPONSE, new DeflateResponseInterceptor(nowrap, weight));
        return this;

    }

    /**
     * Set a CookieStore
     *
     * @param foxHttpCookieStore A CookieStore
     * @return FoxHttpClientBuilder (this)
     */
    public FoxHttpClientBuilder setFoxHttpCookieStore(FoxHttpCookieStore foxHttpCookieStore) {
        foxHttpClient.setFoxHttpCookieStore(foxHttpCookieStore);
        return this;
    }

    /**
     * Set an AuthorizationStrategy
     *
     * @param foxHttpAuthorizationStrategy An AuthorizationStrategy
     * @return FoxHttpClientBuilder (this)
     */
    public FoxHttpClientBuilder setFoxHttpAuthorizationStrategy(FoxHttpAuthorizationStrategy foxHttpAuthorizationStrategy) {
        foxHttpClient.setFoxHttpAuthorizationStrategy(foxHttpAuthorizationStrategy);
        return this;
    }

    /**
     * Add a Authorization to the AuthorizationStrategy
     *
     * @param foxHttpAuthorizationScope Scope of the authorization
     * @param foxHttpAuthorization      Authorization itself
     * @return FoxHttpClientBuilder (this)
     */
    public FoxHttpClientBuilder addFoxHttpAuthorization(FoxHttpAuthorizationScope foxHttpAuthorizationScope, FoxHttpAuthorization foxHttpAuthorization) {
        foxHttpClient.getFoxHttpAuthorizationStrategy().addAuthorization(foxHttpAuthorizationScope, foxHttpAuthorization);
        return this;
    }

    /**
     * Set a TimeoutStrategy
     *
     * @param foxHttpTimeoutStrategy A TimeoutStrategy
     * @return FoxHttpClientBuilder (this)
     */
    public FoxHttpClientBuilder setFoxHttpTimeoutStrategy(FoxHttpTimeoutStrategy foxHttpTimeoutStrategy) {
        foxHttpClient.setFoxHttpTimeoutStrategy(foxHttpTimeoutStrategy);
        return this;
    }

    /**
     * Set undefined timeouts
     *
     * @param connectionTimeout timeout of connection establishment
     * @param readTimeout       timeout of reading response
     * @return FoxHttpClientBuilder (this)
     */
    public FoxHttpClientBuilder setFoxHttpTimeouts(int connectionTimeout, int readTimeout) {
        foxHttpClient.setFoxHttpTimeoutStrategy(new UserDefinedTimeoutStrategy(connectionTimeout, readTimeout));
        return this;
    }

    /**
     * Set a host trust strategy
     *
     * @param foxHttpHostTrustStrategy a host trust strategy
     * @return FoxHttpClientBuilder (this)
     */
    public FoxHttpClientBuilder setFoxHttpHostTrustStrategy(FoxHttpHostTrustStrategy foxHttpHostTrustStrategy) {
        foxHttpClient.setFoxHttpHostTrustStrategy(foxHttpHostTrustStrategy);
        return this;
    }

    /**
     * Set a ssl trust strategy
     *
     * @param foxHttpSSLTrustStrategy a ssl trust strategy
     * @return FoxHttpClientBuilder (this)
     */
    public FoxHttpClientBuilder setFoxHttpSSLTrustStrategy(FoxHttpSSLTrustStrategy foxHttpSSLTrustStrategy) {
        foxHttpClient.setFoxHttpSSLTrustStrategy(foxHttpSSLTrustStrategy);
        return this;
    }

    /**
     * Set a proxy strategy
     *
     * @param foxHttpProxyStrategy a proxy strategy
     * @return FoxHttpClientBuilder (this)
     */
    public FoxHttpClientBuilder setFoxHttpProxyStrategy(FoxHttpProxyStrategy foxHttpProxyStrategy) {
        foxHttpClient.setFoxHttpProxyStrategy(foxHttpProxyStrategy);
        return this;
    }

    /**
     * Set a placeholder strategy
     *
     * @param foxHttpPlaceholderStrategy a placeholder strategy
     * @return FoxHttpClientBuilder (this)
     */
    public FoxHttpClientBuilder setFoxHttpPlaceholderStrategy(FoxHttpPlaceholderStrategy foxHttpPlaceholderStrategy) {
        foxHttpClient.setFoxHttpPlaceholderStrategy(foxHttpPlaceholderStrategy);
        return this;
    }

    /**
     * Add a FoxHttpPlaceholderEntry to the FoxHttpPlaceholderStrategy
     *
     * @param placeholder name of the placeholder (without escape char)
     * @param value       value of the placeholder
     * @return FoxHttpClientBuilder (this)
     */
    public FoxHttpClientBuilder addFoxHttpPlaceholderEntry(String placeholder, String value) {
        foxHttpClient.getFoxHttpPlaceholderStrategy().addPlaceholder(placeholder, value);
        return this;
    }

    /**
     * Set a Logger
     *
     * @param foxHttpLogger A logger
     * @return FoxHttpClientBuilder (this)
     */
    public FoxHttpClientBuilder setFoxHttpLogger(FoxHttpLogger foxHttpLogger) {
        foxHttpClient.setFoxHttpLogger(foxHttpLogger);
        return this;
    }

    /**
     * Activate defined Logger
     *
     * @param activate activate logger
     * @return FoxHttpClientBuilder (this)
     */
    public FoxHttpClientBuilder activteFoxHttpLogger(boolean activate) {
        foxHttpClient.getFoxHttpLogger().setLoggingEnabled(activate);
        return this;
    }

    /**
     * Set the user agent
     *
     * @param foxHttpUserAgent user agent
     * @return FoxHttpClientBuilder (this)
     */
    public FoxHttpClientBuilder setFoxHttpUserAgent(String foxHttpUserAgent) {
        foxHttpClient.setFoxHttpUserAgent(foxHttpUserAgent);
        return this;
    }

    /**
     * Get the FoxHttpClient of this builder
     *
     * @return FoxHttpClient
     */
    public FoxHttpClient build() {
        return this.foxHttpClient;
    }
}
