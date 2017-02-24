package ch.viascom.groundwork.foxhttp;

import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorization;
import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationScope;
import ch.viascom.groundwork.foxhttp.body.request.FoxHttpRequestBody;
import ch.viascom.groundwork.foxhttp.body.request.FoxHttpRequestBodyContext;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.header.FoxHttpHeader;
import ch.viascom.groundwork.foxhttp.header.HeaderEntry;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptorExecutor;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestConnectionInterceptorContext;
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
import java.net.*;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpRequest {

    @Getter
    private URL url;

    private FoxHttpAuthorizationScope authScope;

    @Getter
    @Setter
    private FoxHttpRequestQuery requestQuery = new FoxHttpRequestQuery();

    @Getter
    @Setter
    private FoxHttpRequestBody requestBody;

    @Getter
    @Setter
    private FoxHttpHeader requestHeader = new FoxHttpHeader();

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
    private FoxHttpResponse foxHttpResponse;

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

    public void setUrl(String url) throws MalformedURLException, FoxHttpRequestException {
        if (foxHttpClient == null) {
            throw new FoxHttpRequestException("FoxHttpClient can not be null");
        }
        String parsedURL = foxHttpClient.getFoxHttpPlaceholderStrategy().processPlaceholders(url, foxHttpClient);
        this.url = new URL(parsedURL);
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    /**
     * Execute a this request
     *
     * @return Response if this request
     * @throws FoxHttpException
     */
    public FoxHttpResponse execute() throws FoxHttpException {
        return execute(foxHttpClient);
    }

    /**
     * Execute a this request
     *
     * @param foxHttpClient a specific client which will be used for this request
     * @return Response if this request
     * @throws FoxHttpException
     */
    public FoxHttpResponse execute(FoxHttpClient foxHttpClient) throws FoxHttpException {
        verifyRequest();
        foxHttpClient.getFoxHttpLogger().log("========= Request =========");
        foxHttpClient.getFoxHttpLogger().log("setFoxHttpClient(" + foxHttpClient + ")");
        this.foxHttpClient = foxHttpClient;

        return executeHttp("https".equals(url.getProtocol()));
    }


    private FoxHttpResponse executeHttp(boolean isHttps) throws FoxHttpException {
        try {
            //Execute interceptor
            foxHttpClient.getFoxHttpLogger().log("executeRequestInterceptor()");
            FoxHttpInterceptorExecutor.executeRequestInterceptor(new FoxHttpRequestInterceptorContext(url, this, foxHttpClient));

            foxHttpClient.getFoxHttpLogger().log("setCookieStore(" + foxHttpClient.getFoxHttpCookieStore() + ")");
            CookieHandler.setDefault((CookieManager) foxHttpClient.getFoxHttpCookieStore());

            // Create Scope
            authScope = FoxHttpAuthorizationScope.create(url.toString(), requestType);

            foxHttpClient.getFoxHttpLogger().log("prepareQuery(" + getRequestQuery() + ")");
            prepareQuery();

            foxHttpClient.getFoxHttpLogger().log("processPlaceholders()");
            String parsedURL = foxHttpClient.getFoxHttpPlaceholderStrategy().processPlaceholders(url.toString(), foxHttpClient);
            url = new URL(parsedURL);

            checkPlaceholders();

            //Execute interceptor
            foxHttpClient.getFoxHttpLogger().log("executeRequestConnectionInterceptor()");
            FoxHttpInterceptorExecutor.executeRequestConnectionInterceptor(new FoxHttpRequestConnectionInterceptorContext(url, this, foxHttpClient));

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
            prepareHeader();

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
            processAuthorizationStrategy();

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
                setRequestBodyStream();
            }

            foxHttpClient.getFoxHttpLogger().log("sendRequest()");
            connection.connect();

            foxHttpClient.getFoxHttpLogger().log("========= Response =========");

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
                foxHttpResponse = new FoxHttpResponse(is, this, responseCode, foxHttpClient);
                //Process response headers
                foxHttpClient.getFoxHttpLogger().log("processResponseHeader()");
                processResponseHeader();

                //Execute interceptor
                foxHttpClient.getFoxHttpLogger().log("executeResponseInterceptor()");
                FoxHttpInterceptorExecutor.executeResponseInterceptor(
                        new FoxHttpResponseInterceptorContext(responseCode, foxHttpResponse, this, foxHttpClient)
                );

                return foxHttpResponse;
            } else {
                foxHttpClient.getFoxHttpLogger().log("No response return because skipResponseBody is active!");
                return null;
            }
        } catch (FoxHttpException e) {
            throw e;
        } catch (Exception e) {
            throw new FoxHttpRequestException(e);
        } finally {

            if (connection != null) {
                ((HttpURLConnection) connection).disconnect();
            }
        }
    }

    private void checkPlaceholders() throws FoxHttpRequestException {
        Pattern pattern = Pattern.compile(foxHttpClient.getFoxHttpPlaceholderStrategy().getPlaceholderMatchRegex());
        Matcher matcher = pattern.matcher(url.toString());
        if (matcher.find()) {
            throw new FoxHttpRequestException("The url dose still contain placeholders after finishing processing all defined placeholders.\n-> " + url.toString());
        }

    }

    private void prepareHeader() {
        for (HeaderEntry headerField : getRequestHeader()) {
            connection.addRequestProperty(headerField.getName(), headerField.getValue());
        }
    }

    private void setRequestBodyStream() throws FoxHttpRequestException {
        requestBody.setBody(new FoxHttpRequestBodyContext(connection, this, foxHttpClient));
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

    private void processAuthorizationStrategy() throws FoxHttpRequestException {
        List<FoxHttpAuthorization> foxHttpAuthorizations = foxHttpClient.getFoxHttpAuthorizationStrategy().getAuthorization(connection, authScope, foxHttpClient
        );
        for (FoxHttpAuthorization foxHttpAuthorization : foxHttpAuthorizations) {
            foxHttpClient.getFoxHttpLogger().log("-> doAuthorization(" + foxHttpAuthorization + ")");
            foxHttpAuthorization.doAuthorization(connection, authScope);
        }
    }

    private void processResponseHeader() {
        FoxHttpHeader responseHeaders = new FoxHttpHeader();
        Map<String, List<String>> map = connection.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            if (entry.getKey() != null) {
                responseHeaders.addHeader(entry.getKey(), entry.getValue().get(0));
                foxHttpClient.getFoxHttpLogger().log("-> ResponseHeader(" + entry.getKey() + ":" + entry.getValue().get(0) + ")");
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
