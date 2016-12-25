package ch.viascom.groundwork.foxhttp;

import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorization;
import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationScope;
import ch.viascom.groundwork.foxhttp.body.request.FoxHttpRequestBody;
import ch.viascom.groundwork.foxhttp.body.request.FoxHttpRequestBodyContext;
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
import ch.viascom.groundwork.foxhttp.type.HeaderTypes;
import ch.viascom.groundwork.foxhttp.type.RequestType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.Serializable;
import java.net.*;
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

    @Getter(AccessLevel.PROTECTED)
    private URLConnection connection;


    public FoxHttpRequest() {
        this.foxHttpClient = new FoxHttpClient();
    }

    public FoxHttpRequest(FoxHttpClient foxHttpClient) {
        this.foxHttpClient = foxHttpClient;
    }

    /**
     * Execute a this request
     *
     * @return Response if this request
     * @throws FoxHttpException
     */
    public FoxHttpResponse<T> execute() throws FoxHttpException {
        return execute(foxHttpClient);
    }

    /**
     * Execute a this request
     *
     * @param foxHttpClient a specific client which will be used for this request
     * @return Response if this request
     * @throws FoxHttpException
     */
    public FoxHttpResponse<T> execute(FoxHttpClient foxHttpClient) throws FoxHttpException {
        verifyRequest();
        foxHttpClient.getFoxHttpLogger().log("========= Request =========");

        foxHttpClient.getFoxHttpLogger().log("setFoxHttpClient(" + foxHttpClient + ")");
        this.foxHttpClient = foxHttpClient;

        return executeHttp("https".equals(url.getProtocol()));
    }

    private FoxHttpResponse<T> executeHttp(boolean isHttps) throws FoxHttpRequestException {
        try {
            //Execute interceptor
            foxHttpClient.getFoxHttpLogger().log("executeRequestInterceptor()");
            FoxHttpInterceptorExecutor.executeRequestInterceptor(new FoxHttpRequestInterceptorContext(url, this, foxHttpClient));

            foxHttpClient.getFoxHttpLogger().log("setCookieStore(" + foxHttpClient.getFoxHttpCookieStore() + ")");
            CookieHandler.setDefault((CookieManager) foxHttpClient.getFoxHttpCookieStore());

            foxHttpClient.getFoxHttpLogger().log("prepareQuery(" + getRequestQuery() + ")");
            prepareQuery();

            //Create connection
            foxHttpClient.getFoxHttpLogger().log("createConnection(" + url + ")");
            if (foxHttpClient.getFoxHttpProxyStrategy() == null) {
                connection = url.openConnection();
            } else {
                foxHttpClient.getFoxHttpLogger().log("useProxy(" + foxHttpClient.getFoxHttpProxyStrategy() + ")");
                connection = url.openConnection(foxHttpClient.getFoxHttpProxyStrategy().getProxy(url));
                if (foxHttpClient.getFoxHttpProxyStrategy().hasProxyAuthorization(url)) {
                    setHeaderIfNotExist(HeaderTypes.PROXY_AUTHORIZATION, foxHttpClient.getFoxHttpProxyStrategy().getProxyAuthorization(url), connection);
                }
            }

            foxHttpClient.getFoxHttpLogger().log("setRequestMethod(" + requestType.toString() + ")");
            ((HttpURLConnection) connection).setRequestMethod(requestType.toString());

            //Set headers
            foxHttpClient.getFoxHttpLogger().log("prepareHeader(" + getRequestHeader() + ")");
            prepareHeader(connection);

            //Set User-Agent if not exist
            foxHttpClient.getFoxHttpLogger().log("setUserAgentIfNotExist(" + foxHttpClient.getFoxHttpUserAgent() + ")");
            setHeaderIfNotExist(HeaderTypes.USER_AGENT, foxHttpClient.getFoxHttpUserAgent(), connection);


            connection.setUseCaches(false);
            connection.setDoInput(true);
            foxHttpClient.getFoxHttpLogger().log("setDoOutput(" + doOutput() + ")");
            connection.setDoOutput(doOutput());
            foxHttpClient.getFoxHttpLogger().log("setFollowRedirects(" + followRedirect + ")");
            ((HttpURLConnection) connection).setInstanceFollowRedirects(followRedirect);
            HttpURLConnection.setFollowRedirects(followRedirect);
            foxHttpClient.getFoxHttpLogger().log("setFoxHttpTimeoutStrategy(" + foxHttpClient.getFoxHttpTimeoutStrategy() + ")");
            connection.setConnectTimeout(foxHttpClient.getFoxHttpTimeoutStrategy().getConnectionTimeout());
            connection.setReadTimeout(foxHttpClient.getFoxHttpTimeoutStrategy().getReadTimeout());

            if (isHttps) {
                foxHttpClient.getFoxHttpLogger().log("setSSLSocketFactory(" + foxHttpClient.getFoxHttpSSLTrustStrategy() + ")");
                ((HttpsURLConnection) connection).setSSLSocketFactory(foxHttpClient.getFoxHttpSSLTrustStrategy().getSSLSocketFactory((HttpsURLConnection) connection));
                foxHttpClient.getFoxHttpLogger().log("setHostnameVerifier(" + foxHttpClient.getFoxHttpHostTrustStrategy() + ")");
                ((HttpsURLConnection) connection).setHostnameVerifier(foxHttpClient.getFoxHttpHostTrustStrategy());
            }

            //Process authorization strategy
            foxHttpClient.getFoxHttpLogger().log("processAuthorizationStrategy(" + foxHttpClient.getFoxHttpAuthorizationStrategy() + ")");
            processAuthorizationStrategy(connection);

            //Execute interceptor
            foxHttpClient.getFoxHttpLogger().log("executeRequestHeaderInterceptor()");
            FoxHttpInterceptorExecutor.executeRequestHeaderInterceptor(new FoxHttpRequestHeaderInterceptorContext(connection, this, foxHttpClient));

            //Send request
            if (doOutput()) {
                //Add Content-Type header if not exist
                foxHttpClient.getFoxHttpLogger().log("setContentTypeIfNotExist(" + requestBody.getOutputContentType().toString() + ")");
                setHeaderIfNotExist(HeaderTypes.CONTENT_TYPE, requestBody.getOutputContentType().toString(), connection);
                //Set request body
                foxHttpClient.getFoxHttpLogger().log("setRequestBodyStream(" + getRequestBody() + ")");
                setRequestBodyStream(connection);
            }

            foxHttpClient.getFoxHttpLogger().log("sendRequest()");
            connection.connect();

            int responseCode = ((HttpURLConnection) connection).getResponseCode();
            foxHttpClient.getFoxHttpLogger().log("responseCode(" + responseCode + ")");

            if (!skipResponseBody) {
                InputStream is;
                if (responseCode >= HttpURLConnection.HTTP_OK && responseCode < HttpURLConnection.HTTP_MULT_CHOICE) {
                    //On success response code
                    foxHttpClient.getFoxHttpLogger().log("getResponseBody(success)");
                    is = connection.getInputStream();
                } else {
                    //On error response code
                    foxHttpClient.getFoxHttpLogger().log("getResponseBody(error)");
                    is = ((HttpURLConnection) connection).getErrorStream();
                }

                //Execute interceptor
                foxHttpClient.getFoxHttpLogger().log("executeResponseCodeInterceptor()");
                FoxHttpInterceptorExecutor.executeResponseCodeInterceptor(
                        new FoxHttpResponseCodeInterceptorContext(responseCode, this, foxHttpClient)
                );

                foxHttpClient.getFoxHttpLogger().log("createFoxHttpResponse()");
                foxHttpResponse = new FoxHttpResponse<>(is, this, responseCode, foxHttpClient);
                //Process response headers
                foxHttpClient.getFoxHttpLogger().log("processResponseHeader()");
                processResponseHeader(foxHttpResponse, connection);

                //Execute interceptor
                foxHttpClient.getFoxHttpLogger().log("executeResponseInterceptor()");
                FoxHttpInterceptorExecutor.executeResponseInterceptor(
                        new FoxHttpResponseInterceptorContext(responseCode, foxHttpResponse, this, foxHttpClient)
                );

                return foxHttpResponse;
            } else {
                return null;
            }
        } catch (FoxHttpRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new FoxHttpRequestException(e);
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

    private void setRequestBodyStream(URLConnection urlConnection) throws FoxHttpRequestException {
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

    private void processAuthorizationStrategy(URLConnection connection) throws FoxHttpRequestException {
        List<FoxHttpAuthorization> foxHttpAuthorizations = foxHttpClient.getFoxHttpAuthorizationStrategy().getAuthorization(connection, FoxHttpAuthorizationScope.create(
                url.toString(), RequestType.valueOf(((HttpURLConnection) connection).getRequestMethod()))
        );
        for (FoxHttpAuthorization foxHttpAuthorization : foxHttpAuthorizations) {
            foxHttpClient.getFoxHttpLogger().log("-> doAuthorization(" + foxHttpAuthorization + ")");
            foxHttpAuthorization.doAuthorization(connection, FoxHttpAuthorizationScope.create(
                    url.toString(), RequestType.valueOf(((HttpURLConnection) connection).getRequestMethod()))
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
        return requestBody != null && requestBody.hasBody();
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
        foxHttpClient.getFoxHttpLogger().log("verifyRequest()");
    }
}
