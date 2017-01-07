package ch.viascom.groundwork.foxhttp.builder;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
import ch.viascom.groundwork.foxhttp.body.request.FoxHttpRequestBody;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.header.FoxHttpRequestHeader;
import ch.viascom.groundwork.foxhttp.header.HeaderEntry;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptorType;
import ch.viascom.groundwork.foxhttp.query.FoxHttpRequestQuery;
import ch.viascom.groundwork.foxhttp.type.HeaderTypes;
import ch.viascom.groundwork.foxhttp.type.RequestType;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * FoxHttpRequest builder to create a new FoxHttpRequest
 *
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpRequestBuilder {

    private FoxHttpRequest foxHttpRequest;
    private String url;

    // -- Constructors

    /**
     * Create a new builder with a default request
     */
    public FoxHttpRequestBuilder() {
        foxHttpRequest = new FoxHttpRequest();
    }

    /**
     * Create a new builder with a default request and set the url
     *
     * @param url url of the request
     */
    public FoxHttpRequestBuilder(URL url) throws MalformedURLException {
        this(url.toString());
    }

    /**
     * Create a new builder with a default request and set the url
     *
     * @param url url of the request
     * @throws MalformedURLException If the url is not well formed
     */
    public FoxHttpRequestBuilder(String url) throws MalformedURLException {
        this(url, RequestType.GET);
    }

    /**
     * Create a new builder with a default request and set the url and request type
     *
     * @param url         url of the request
     * @param requestType request type
     */
    public FoxHttpRequestBuilder(URL url, RequestType requestType) throws MalformedURLException {
        this(url.toString(), requestType);
    }

    /**
     * Create a new builder with a default request and set the url
     *
     * @param url         url of the request
     * @param requestType request type
     * @throws MalformedURLException If the url is not well formed
     */
    public FoxHttpRequestBuilder(String url, RequestType requestType) throws MalformedURLException {
        this(url, requestType, new FoxHttpClient());
    }

    /**
     * Create a new builder with a default request and set the url, request type and FoxHttpClient
     *
     * @param url           url of the request
     * @param requestType   request type
     * @param foxHttpClient FoxHttpClient in which the request gets executed
     */
    public FoxHttpRequestBuilder(URL url, RequestType requestType, FoxHttpClient foxHttpClient) throws MalformedURLException {
        this(url.toString(), requestType, foxHttpClient);
    }

    /**
     * Create a new builder with a default request and set the url, request type and FoxHttpClient
     *
     * @param url           url of the request
     * @param requestType   request type
     * @param foxHttpClient FoxHttpClient in which the request gets executed
     * @throws MalformedURLException If the url is not well formed
     */
    public FoxHttpRequestBuilder(String url, RequestType requestType, FoxHttpClient foxHttpClient) throws MalformedURLException {
        foxHttpRequest = new FoxHttpRequest(foxHttpClient);
        this.url = url;
        foxHttpRequest.setRequestType(requestType);
    }


    // -- Setters

    /**
     * Set a FoxHttpClient
     *
     * @param foxHttpClient a FoxHttpClient
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder setFoxHttpClient(FoxHttpClient foxHttpClient) {
        foxHttpRequest.setFoxHttpClient(foxHttpClient);
        return this;
    }

    /**
     * Set a request type
     *
     * @param requestType a request type
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder setRequestType(RequestType requestType) {
        foxHttpRequest.setRequestType(requestType);
        return this;
    }

    /**
     * Set a FoxHttpRequestQuery
     *
     * @param foxHttpRequestQuery a FoxHttpRequestQuery
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder setRequestQuery(FoxHttpRequestQuery foxHttpRequestQuery) {
        foxHttpRequest.setRequestQuery(foxHttpRequestQuery);
        return this;
    }

    /**
     * Add a new query entry
     *
     * @param name  name of the query entry
     * @param value value of the query entry
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder addRequestQueryEntry(String name, String value) {
        foxHttpRequest.getRequestQuery().addQueryEntry(name, value);
        return this;
    }

    /**
     * Set a body for this request
     * <i>Do not set this if you have a request type other than POST or PUT</i>
     *
     * @param foxHttpRequestBody a body
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder setRequestBody(FoxHttpRequestBody foxHttpRequestBody) {
        foxHttpRequest.setRequestBody(foxHttpRequestBody);
        return this;
    }

    /**
     * Set a header for this request
     *
     * @param foxHttpRequestHeader a header
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder setRequestHeader(FoxHttpRequestHeader foxHttpRequestHeader) {
        foxHttpRequest.setRequestHeader(foxHttpRequestHeader);
        return this;
    }

    /**
     * Add a new header entry
     *
     * @param headerField a header field
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder addRequestHeader(HeaderEntry headerField) {
        foxHttpRequest.getRequestHeader().addHeader(headerField.getName(), headerField.getValue());
        return this;
    }

    /**
     * Add a new header entry
     *
     * @param name  name of the header entry
     * @param value value of the header entry
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder addRequestHeader(String name, String value) {
        foxHttpRequest.getRequestHeader().addHeader(name, value);
        return this;
    }

    /**
     * Add a new header entry
     *
     * @param name  name of the header entry
     * @param value value of the header entry
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder addRequestHeader(HeaderTypes name, String value) {
        foxHttpRequest.getRequestHeader().addHeader(name, value);
        return this;
    }

    /**
     * Sets if the response body should be skiped
     *
     * @param skipResponseBody should skip response body?
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder setSkipResponseBody(boolean skipResponseBody) {
        foxHttpRequest.setSkipResponseBody(skipResponseBody);
        return this;
    }

    /**
     * Sets if the request should follow redirects
     *
     * @param followRedirect should follow redirects?
     * @return FoxHttpRequestBuilder (this)
     */
    public FoxHttpRequestBuilder setFollowRedirect(boolean followRedirect) {
        foxHttpRequest.setFollowRedirect(followRedirect);
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
    public FoxHttpRequestBuilder registerFoxHttpInterceptor(FoxHttpInterceptorType interceptorType, FoxHttpInterceptor foxHttpInterceptor) throws FoxHttpException {
        foxHttpRequest.getFoxHttpClient().register(interceptorType, foxHttpInterceptor);
        return this;
    }

    /**
     * Add a FoxHttpPlaceholderEntry to the FoxHttpPlaceholderStrategy
     *
     * @param placeholder name of the placeholder (without escape char)
     * @param value       value of the placeholder
     * @return FoxHttpClientBuilder (this)
     */
    public FoxHttpRequestBuilder addFoxHttpPlaceholderEntry(String placeholder, String value) {
        foxHttpRequest.getFoxHttpClient().getFoxHttpPlaceholderStrategy().addPlaceholder(placeholder, value);
        return this;
    }

    /**
     * Get the FoxHttpRequest of this builder
     *
     * @return FoxHttpRequest
     */
    public FoxHttpRequest build() throws MalformedURLException {
        if (this.url != null) {
            foxHttpRequest.setUrl(this.url);
        }
        return foxHttpRequest;
    }

}
