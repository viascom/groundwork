package ch.viascom.groundwork.foxhttp;

import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorization;
import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationScope;
import ch.viascom.groundwork.foxhttp.body.FoxHttpRequestBodyContext;
import ch.viascom.groundwork.foxhttp.body.request.FoxHttpRequestBody;
import ch.viascom.groundwork.foxhttp.body.response.FoxHttpResponseInformation;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.header.FoxHttpHeader;
import ch.viascom.groundwork.foxhttp.header.FoxHttpRequestHeader;
import ch.viascom.groundwork.foxhttp.header.HeaderField;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptorExecutor;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestHeaderInterceptorContext;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestInterceptorContext;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseCodeInterceptorContext;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseInterceptorContext;
import ch.viascom.groundwork.foxhttp.query.FoxHttpRequestQuery;
import ch.viascom.groundwork.foxhttp.type.ContentType;
import ch.viascom.groundwork.foxhttp.type.HeaderTypes;
import ch.viascom.groundwork.foxhttp.type.RequestType;
import lombok.Getter;
import lombok.Setter;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.Serializable;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpRequest<T extends Serializable> {

    @Getter
    @Setter
    private URL url;

    @Getter
    @Setter
    private FoxHttpRequestQuery requestQuery = new FoxHttpRequestQuery();

    @Getter
    @Setter
    private FoxHttpRequestBody requestBody;

    @Getter
    @Setter
    private FoxHttpRequestHeader requestHeader = new FoxHttpRequestHeader();

    @Getter
    @Setter
    private RequestType requestType = RequestType.GET;

    @Getter
    @Setter
    private boolean skipResponseBody = false;

    @Getter
    @Setter
    private boolean followRedirect = true;

    @Getter
    private FoxHttpResponse<T> foxHttpResponse;

    @Getter
    @Setter
    private FoxHttpClient foxHttpClient;

    private URLConnection connection;


    public FoxHttpRequest() {
        this.foxHttpClient = new FoxHttpClient();
    }

    public FoxHttpRequest(FoxHttpClient foxHttpClient) {
        this.foxHttpClient = foxHttpClient;
    }

    public FoxHttpResponse<T> execute() throws Exception {
        return execute(foxHttpClient);
    }

    public FoxHttpResponse<T> execute(FoxHttpClient foxHttpClient) throws Exception {

        this.foxHttpClient = foxHttpClient;

        verifyRequest();

        return executeHttp((url.getProtocol().equals("https")));
    }

    private FoxHttpResponse<T> executeHttp(boolean isHttps) throws FoxHttpRequestException {
        try {
            //Execute interceptor
            FoxHttpInterceptorExecutor.executeRequestInterceptor(new FoxHttpRequestInterceptorContext(url, this, foxHttpClient));

            CookieHandler.setDefault((CookieManager) foxHttpClient.getFoxHttpCookieStore());
            prepareQuery();

            //Create connection
            if (foxHttpClient.getFoxHttpProxyStrategy() == null) {
                connection = url.openConnection();
            } else {
                connection = url.openConnection(foxHttpClient.getFoxHttpProxyStrategy().getProxy(url));
                if (foxHttpClient.getFoxHttpProxyStrategy().hasProxyAuthorization(url)) {
                    setHeaderIfNotExist(HeaderTypes.PROXY_AUTHORIZATION, foxHttpClient.getFoxHttpProxyStrategy().getProxyAuthorization(url), connection);
                }
            }
            ((HttpURLConnection) connection).setRequestMethod(requestType.toString());
            //Set headers
            prepareHeader(connection);
            //Set User-Agent if not exist
            setHeaderIfNotExist(HeaderTypes.USER_AGENT, foxHttpClient.getFoxHttpUserAgent(), connection);

            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(doOutput());
            ((HttpURLConnection) connection).setInstanceFollowRedirects(followRedirect);
            HttpURLConnection.setFollowRedirects(followRedirect);
            connection.setConnectTimeout(foxHttpClient.getFoxHttpTimeoutStrategy().getConnectionTimeout());
            connection.setReadTimeout(foxHttpClient.getFoxHttpTimeoutStrategy().getReadTimeout());

            if (isHttps) {
                ((HttpsURLConnection) connection).setSSLSocketFactory(foxHttpClient.getFoxHttpSSLTrustStrategy().getSSLSocketFactory(((HttpsURLConnection) connection)));
                ((HttpsURLConnection) connection).setHostnameVerifier(foxHttpClient.getFoxHttpHostTrustStrategy());
            }

            //Process authorization strategy
            processAuthorizationStrategy(connection);

            //Execute interceptor
            FoxHttpInterceptorExecutor.executeRequestHeaderInterceptor(new FoxHttpRequestHeaderInterceptorContext(connection, this, foxHttpClient));

            //Send request
            if (doOutput()) {
                //Add Content-Type header if not exist
                setHeaderIfNotExist(HeaderTypes.CONTENT_TYPE, requestBody.getOutputContentType().toString(), connection);
                //Set request body
                setRequestBodyStream(connection);
            }

            connection.connect();

            if (!skipResponseBody) {
                InputStream is;
                int responseCode = ((HttpURLConnection) connection).getResponseCode();
                if (responseCode >= HttpURLConnection.HTTP_OK && responseCode < HttpURLConnection.HTTP_MULT_CHOICE) {
                    //On success response code
                    is = connection.getInputStream();
                } else {
                    //On error response code
                    is = ((HttpURLConnection) connection).getErrorStream();
                }

                //Execute interceptor
                FoxHttpInterceptorExecutor.executeResponseCodeInterceptor(
                        new FoxHttpResponseCodeInterceptorContext(responseCode, this, foxHttpClient)
                );

                foxHttpResponse = new FoxHttpResponse<>(is, this, responseCode, foxHttpClient, new FoxHttpResponseInformation(url, requestType));
                //Process response headers
                processResponseHeader(foxHttpResponse, connection);

                //Execute interceptor
                FoxHttpInterceptorExecutor.executeResponseInterceptor(
                        new FoxHttpResponseInterceptorContext(responseCode, foxHttpResponse, this, foxHttpClient)
                );

                return foxHttpResponse;
            } else {
                return null;
            }
        } catch (Exception e) {
            //Error handling
            if (e instanceof FoxHttpRequestException) {
                throw (FoxHttpRequestException) e;
            } else {
                throw new FoxHttpRequestException(e);
            }
        } finally {

            if (connection != null) {
                ((HttpURLConnection) connection).disconnect();
            }
        }
    }

    private void prepareHeader(URLConnection connection) {
        for (HeaderField headerField : getRequestHeader()) {
            connection.addRequestProperty(headerField.getName(), headerField.getValue());
        }
    }

    private void setRequestBodyStream(URLConnection urlConnection) throws Exception {
        requestBody.setBody(new FoxHttpRequestBodyContext(urlConnection, this, foxHttpClient));
    }

    private void prepareQuery() throws FoxHttpRequestException, MalformedURLException {
        if (getRequestQuery().hasQueryEntries()) {
            String query = getRequestQuery().getQueryString();
            url = new URL(url.toString() + query);
        }
    }

    private void setHeaderIfNotExist(HeaderTypes type, String value, URLConnection connection) {
        if (connection.getRequestProperty(type.toString()) == null) {
            connection.setRequestProperty(type.toString(), value);
        }
    }

    private void processAuthorizationStrategy(URLConnection connection) {
        ArrayList<FoxHttpAuthorization> foxHttpAuthorizations = foxHttpClient.getFoxHttpAuthorizationStrategy().getAuthorization(connection, FoxHttpAuthorizationScope.create(
                url, RequestType.valueOf(((HttpURLConnection) connection).getRequestMethod()))
        );
        for (FoxHttpAuthorization foxHttpAuthorization : foxHttpAuthorizations) {
            foxHttpAuthorization.doAuthorization(connection, FoxHttpAuthorizationScope.create(
                    url, RequestType.valueOf(((HttpURLConnection) connection).getRequestMethod()))
            );
        }
    }

    private void processResponseHeader(FoxHttpResponse<T> foxHttpResponse, URLConnection connection) {
        FoxHttpHeader responseHeaders = new FoxHttpHeader();
        Map<String, List<String>> map = connection.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            if (entry.getKey() != null) {
                responseHeaders.addHeader(entry.getKey(), entry.getValue().get(0));
            }
        }

        foxHttpResponse.setResponseHeaders(responseHeaders);
    }

    private boolean doOutput() {
        return (requestBody != null && requestBody.hasBody());
    }

    private void verifyRequest() throws FoxHttpException {
        //Check for body in GET and DELETE
        if ((requestType == RequestType.GET || requestType == RequestType.DELETE) && (requestBody != null && requestBody.hasBody())) {
            throw new FoxHttpRequestException("Request type '" + requestType + "' does not allow a request body!");
        }

        //Check for empty URL
        if (url == null) {
            throw new FoxHttpRequestException("URL of the request ist not defined");
        }

        //Check for FoxHttpClient
        if (foxHttpClient == null) {
            throw new FoxHttpRequestException("FoxHttpClient of the request ist not defined");
        }
    }


    public void addRequestHeader(String name, String value) {
        this.getRequestHeader().addHeader(name, value);
    }

    public void addRequestHeader(HeaderTypes name, String value) {
        this.getRequestHeader().addHeader(name, value);
    }

    public void addRequestHeader(String name, ContentType value) {
        this.getRequestHeader().addHeader(name, value.toString());
    }

    public void addRequestHeader(HeaderTypes name, ContentType value) {
        this.getRequestHeader().addHeader(name, value.toString());
    }
}
