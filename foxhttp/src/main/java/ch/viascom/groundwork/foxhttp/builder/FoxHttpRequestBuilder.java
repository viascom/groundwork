package ch.viascom.groundwork.foxhttp.builder;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
import ch.viascom.groundwork.foxhttp.body.request.FoxHttpRequestBody;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.header.FoxHttpRequestHeader;
import ch.viascom.groundwork.foxhttp.header.HeaderField;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptorType;
import ch.viascom.groundwork.foxhttp.query.FoxHttpRequestQuery;
import ch.viascom.groundwork.foxhttp.type.HeaderTypes;
import ch.viascom.groundwork.foxhttp.type.RequestType;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpRequestBuilder<T extends Serializable> {

    private FoxHttpRequest<T> foxHttpRequest;


    // -- Constructors
    public FoxHttpRequestBuilder() {
        foxHttpRequest = new FoxHttpRequest<>();
    }

    public FoxHttpRequestBuilder(String url) throws MalformedURLException {
        this(new URL(url));
    }

    public FoxHttpRequestBuilder(URL url) {
        this(url, RequestType.GET);
    }

    public FoxHttpRequestBuilder(String url, RequestType requestType) throws MalformedURLException {
        this(new URL(url), requestType);
    }

    public FoxHttpRequestBuilder(URL url, RequestType requestType) {
        this(url, requestType, new FoxHttpClient());
    }

    public FoxHttpRequestBuilder(String url, RequestType requestType, FoxHttpClient foxHttpClient) throws MalformedURLException {
        this(new URL(url), requestType, foxHttpClient);
    }

    public FoxHttpRequestBuilder(URL url, RequestType requestType, FoxHttpClient foxHttpClient) {
        foxHttpRequest = new FoxHttpRequest<>(foxHttpClient);
        foxHttpRequest.setUrl(url);
        foxHttpRequest.setRequestType(requestType);
    }


    // -- Setters

    public FoxHttpRequestBuilder setFoxHttpClient(FoxHttpClient foxHttpClient) {
        foxHttpRequest.setFoxHttpClient(foxHttpClient);
        return this;
    }

    public FoxHttpRequestBuilder setRequestType(RequestType requestType) {
        foxHttpRequest.setRequestType(requestType);
        return this;
    }

    public FoxHttpRequestBuilder setRequestQuery(FoxHttpRequestQuery foxHttpRequestQuery) {
        foxHttpRequest.setRequestQuery(foxHttpRequestQuery);
        return this;
    }

    public FoxHttpRequestBuilder addRequestQueryEntry(String name, String value) {
        foxHttpRequest.getRequestQuery().addQueryEntry(name, value);
        return this;
    }

    public FoxHttpRequestBuilder setRequestBody(FoxHttpRequestBody foxHttpRequestBody) {
        foxHttpRequest.setRequestBody(foxHttpRequestBody);
        return this;
    }

    public FoxHttpRequestBuilder setRequestHeader(FoxHttpRequestHeader foxHttpRequestHeader) {
        foxHttpRequest.setRequestHeader(foxHttpRequestHeader);
        return this;
    }

    public FoxHttpRequestBuilder addRequestHeader(HeaderField headerField) {
        foxHttpRequest.addRequestHeader(headerField.getName(), headerField.getValue());
        return this;
    }

    public FoxHttpRequestBuilder addRequestHeader(String name, String value) {
        foxHttpRequest.addRequestHeader(name, value);
        return this;
    }

    public FoxHttpRequestBuilder addRequestHeader(HeaderTypes name, String value) {
        foxHttpRequest.addRequestHeader(name, value);
        return this;
    }

    public FoxHttpRequestBuilder setSkipResponseBody(boolean skipResponseBody) {
        foxHttpRequest.setSkipResponseBody(skipResponseBody);
        return this;
    }

    public FoxHttpRequestBuilder setFollowRedirect(boolean followRedirect) {
        foxHttpRequest.setFollowRedirect(followRedirect);
        return this;
    }

    public FoxHttpRequestBuilder registerInterceptor(FoxHttpInterceptorType interceptorType, FoxHttpInterceptor foxHttpInterceptor) throws FoxHttpException {
        foxHttpRequest.getFoxHttpClient().register(interceptorType, foxHttpInterceptor);
        return this;
    }

    public FoxHttpRequest<T> build() {
        return foxHttpRequest;
    }

}
