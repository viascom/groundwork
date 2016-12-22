package ch.viascom.groundwork.restclient.http.request;


import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.filter.FilterTypes;
import ch.viascom.groundwork.restclient.filter.RESTFilter;
import ch.viascom.groundwork.restclient.filter.response.ErrorResponseCodeFilter;
import ch.viascom.groundwork.restclient.filter.response.SuccessResponseCodeFilter;
import ch.viascom.groundwork.restclient.filter.response.defaults.DefaultErrorResponseCodeFilter;
import ch.viascom.groundwork.restclient.filter.response.defaults.DefaultSuccessResponseCodeFilter;
import ch.viascom.groundwork.restclient.http.request.filter.request.RequestExceptionFilter;
import ch.viascom.groundwork.restclient.http.request.filter.request.RequestFilter;
import ch.viascom.groundwork.restclient.http.request.filter.request.RequestWriteFilter;
import ch.viascom.groundwork.restclient.http.request.filter.response.ResponseExceptionFilter;
import ch.viascom.groundwork.restclient.http.request.filter.response.ResponseFilter;
import ch.viascom.groundwork.restclient.http.request.filter.response.ResponseReadFilter;
import ch.viascom.groundwork.restclient.request.RequestInterface;
import ch.viascom.groundwork.restclient.response.GenericResponse;
import ch.viascom.groundwork.restclient.response.JSONResponse;
import ch.viascom.groundwork.restclient.response.NoContentResponse;
import ch.viascom.groundwork.restclient.response.generic.ErrorResponse;
import ch.viascom.groundwork.restclient.response.generic.Response;
import ch.viascom.groundwork.restclient.response.generic.ResponseHeader;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public abstract class Request<T extends Response> implements RequestInterface<T> {

    private static final Logger log = LogManager.getLogger(Request.class);

    //Request data
    protected String url = "";
    protected String path = "";
    protected URI requestUrl;
    protected String mediaType = "application/json";
    protected HttpEntity requestBody = new StringEntity("", Consts.UTF_8);
    protected HttpClient httpClient;
    protected HttpClientContext httpClientContext = HttpClientContext.create();
    protected HashMap<String, String> queryParamMap = new HashMap<>();
    protected HashMap<String, String> headerMap = new HashMap<>();

    //Status codes
    protected ArrayList<Integer> additionalAllowedStatusCodes = new ArrayList<>();
    protected ArrayList<Integer> additionalDeniedStatusCodes = new ArrayList<>();

    //Filters
    protected HashMap<String, RequestFilter> requestFilters = new HashMap<>();
    protected HashMap<String, RequestWriteFilter> requestWriteFilters = new HashMap<>();
    protected HashMap<String, RequestExceptionFilter> requestExceptionFilters = new HashMap<>();
    protected HashMap<String, ResponseFilter> responseFilters = new HashMap<>();
    protected HashMap<String, ResponseReadFilter> responseReadFilters = new HashMap<>();
    protected HashMap<String, ResponseExceptionFilter> responseExceptionFilters = new HashMap<>();

    protected HashMap<String, SuccessResponseCodeFilter> successResponseCodeFilters = new HashMap<>();
    protected HashMap<String, ErrorResponseCodeFilter> errorResponseCodeFilters = new HashMap<>();


    protected Request(String url, HttpClient httpClient) {
        this.httpClient = httpClient;
        this.url = url;
    }

    public abstract Object request() throws Exception;

    public void addHeaders(String name, String value) {
        this.headerMap.put(name, value);
    }

    public Header[] getRequestHeader() {
        ArrayList<Header> headers = new ArrayList<>();

        for (String key : headerMap.keySet()) {
            BasicHeader header = new BasicHeader(key, headerMap.get(key));
            headers.add(header);
        }
        Header[] simpleArray = new Header[headers.size()];
        headers.toArray(simpleArray);
        return simpleArray;
    }

    public void setRequestParams(ArrayList<String> params, Object o) throws RESTClientException {
        try {
            Class clazz = o.getClass();

            HashMap<String, String> paramMap = new HashMap<>();

            for (String param : params) {
                Field field = clazz.getDeclaredField(param);

                field.setAccessible(true);

                String paramName = field.getName();

                SerializedName anno = field.getAnnotation(SerializedName.class);
                if (anno != null) {
                    paramName = anno.value();
                }

                String value = String.valueOf(field.get(o));

                if (field.get(o) != null && !value.isEmpty()) {
                    paramMap.put(paramName, value);
                }
            }

            queryParamMap = paramMap;
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorMessage(e.getMessage());
            throw new RESTClientException(errorResponse, e.getMessage());
        }
    }

    public void addQueryParam(String name, String value) {
        queryParamMap.put(name, value);
    }

    public void addAdditionalAllowedStatusCode(int code) {
        additionalAllowedStatusCodes.add(code);
    }

    public void addAdditionalDeniedStatusCode(int code) {
        additionalDeniedStatusCodes.add(code);
    }

    public void register(RESTFilter restFilter) {
        if (restFilter instanceof RequestFilter) {
            requestFilters.put(restFilter.getClass().getCanonicalName(), (RequestFilter) restFilter);
        } else if (restFilter instanceof RequestWriteFilter) {
            requestWriteFilters.put(restFilter.getClass().getCanonicalName(), (RequestWriteFilter) restFilter);
        } else if (restFilter instanceof RequestExceptionFilter) {
            requestExceptionFilters.put(restFilter.getClass().getCanonicalName(), (RequestExceptionFilter) restFilter);
        } else if (restFilter instanceof ResponseFilter) {
            responseFilters.put(restFilter.getClass().getCanonicalName(), (ResponseFilter) restFilter);
        } else if (restFilter instanceof ResponseReadFilter) {
            responseReadFilters.put(restFilter.getClass().getCanonicalName(), (ResponseReadFilter) restFilter);
        } else if (restFilter instanceof ResponseExceptionFilter) {
            responseExceptionFilters.put(restFilter.getClass().getCanonicalName(), (ResponseExceptionFilter) restFilter);
        } else if (restFilter instanceof SuccessResponseCodeFilter) {
            successResponseCodeFilters.put(restFilter.getClass().getCanonicalName(), (SuccessResponseCodeFilter) restFilter);
        } else if (restFilter instanceof ErrorResponseCodeFilter) {
            errorResponseCodeFilters.put(restFilter.getClass().getCanonicalName(), (ErrorResponseCodeFilter) restFilter);
        } else {
            throw new IllegalArgumentException("Not supported RESTFilter-Class");
        }
    }

    public void unregister(Class restFilter) {
        requestFilters.remove(restFilter.getCanonicalName());
        requestWriteFilters.remove(restFilter.getCanonicalName());
        requestExceptionFilters.remove(restFilter.getCanonicalName());
        responseFilters.remove(restFilter.getCanonicalName());
        responseReadFilters.remove(restFilter.getCanonicalName());
        responseExceptionFilters.remove(restFilter.getCanonicalName());
        successResponseCodeFilters.remove(restFilter.getCanonicalName());
        errorResponseCodeFilters.remove(restFilter.getCanonicalName());
    }

    public HashMap<String, Object> getRESTFilters() {
        HashMap<String, Object> restFilters = new HashMap<>();
        restFilters.put(RequestFilter.class.getCanonicalName(), requestFilters.values());
        restFilters.put(RequestWriteFilter.class.getCanonicalName(), requestWriteFilters.values());
        restFilters.put(RequestExceptionFilter.class.getCanonicalName(), requestExceptionFilters.values());
        restFilters.put(ResponseFilter.class.getCanonicalName(), responseFilters.values());
        restFilters.put(ResponseReadFilter.class.getCanonicalName(), responseReadFilters.values());
        restFilters.put(ResponseExceptionFilter.class.getCanonicalName(), responseExceptionFilters.values());
        restFilters.put(SuccessResponseCodeFilter.class.getCanonicalName(), successResponseCodeFilters.values());
        restFilters.put(ErrorResponseCodeFilter.class.getCanonicalName(), errorResponseCodeFilters.values());
        return restFilters;
    }

    public ArrayList<RESTFilter> getRESTFilters(FilterTypes filterType) {
        ArrayList<RESTFilter> restFilters = new ArrayList<>();
        if (filterType == FilterTypes.REQUESTFILTER) {
            restFilters.addAll(requestFilters.values());
        } else if (filterType == FilterTypes.REQUESTWRITEFILTER) {
            restFilters.addAll(requestWriteFilters.values());
        } else if (filterType == FilterTypes.REQUESTEXCEPTIONFILTER) {
            restFilters.addAll(requestExceptionFilters.values());
        } else if (filterType == FilterTypes.RESPONSEFILTER) {
            restFilters.addAll(responseFilters.values());
        } else if (filterType == FilterTypes.RESPONSEREADFILTER) {
            restFilters.addAll(responseReadFilters.values());
        } else if (filterType == FilterTypes.RESPONSEEXCEPTIONFILTER) {
            restFilters.addAll(responseExceptionFilters.values());
        } else if (filterType == FilterTypes.SUCCESSRESPONSECODEFILTER) {
            restFilters.addAll(successResponseCodeFilters.values());
        } else if (filterType == FilterTypes.ERRORRESPONSECODEFILTER) {
            restFilters.addAll(errorResponseCodeFilters.values());
        }
        return restFilters;
    }

    public void prepareQuery() throws URISyntaxException {
        URIBuilder builder = new URIBuilder(url + getEncodedPath());
        HashMap<String, String> param = queryParamMap;
        if (param != null && !param.isEmpty()) {
            for (String key : param.keySet()) {
                builder.setParameter(key, param.get(key).toString());
            }
        }
        this.requestUrl = builder.build();
    }

    protected Gson getGson() {
        Gson gson = new Gson();
        return gson;
    }

    public T execute() throws Exception {
        Response output = getParameterClass().newInstance();
        String responseBody = "";
        ResponseHeader responseHeader = new ResponseHeader();
        try {
            HttpResponse response = (HttpResponse) request();
            executeFilter(response, null, null, this, null, null, FilterTypes.RESPONSEREADFILTER);
            int status = response.getStatusLine().getStatusCode();
            responseHeader.setResponseHeaders(convertHeaders(response.getAllHeaders()));
            responseHeader.setStatusCode(status);
            responseHeader.setRequestPath(url + getEncodedPath());
            HttpEntity entity = response.getEntity();

            if (isStatusCodeAllowed(status)) {
                log.debug("-> Response status: " + status);

                if (entity != null && getParameterClass() != NoContentResponse.class) {
                    if (getParameterClass() == GenericResponse.class) {
                        output = new GenericResponse(entity);
                    } else if (getParameterClass() == JSONResponse.class) {
                        output = new JSONResponse(EntityUtils.toString(entity));
                    } else {
                        responseBody = EntityUtils.toString(entity);
                        Gson gson = getGson();
                        output = gson.fromJson(responseBody, getParameterClass());
                    }
                } else {
                    //NoContentResponse
                    output = getParameterClass().newInstance();
                }
                output.setResponseHeader(responseHeader);
                executeFilter(response, null, output, this, null, null, FilterTypes.RESPONSEFILTER);
                executeFilter(response, null, output, this, null, null, FilterTypes.SUCCESSRESPONSECODEFILTER);
                return (T) output;
            } else {
                log.debug("-> Invalid response status: " + status);
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setRequestBody(getRequestBody().toString());
                errorResponse.setResponseBody(responseBody);
                errorResponse.setResponseHeader(responseHeader);
                executeFilter(response, null, output, this, errorResponse, null, FilterTypes.ERRORRESPONSECODEFILTER);
                return (T) output;
            }
        } catch (Exception e) {
            if (e instanceof RESTClientException) {
                //Pass through RESTClientException
                throw e;
            } else {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setErrorMessage(e.getMessage());
                errorResponse.setResponseHeader(responseHeader);
                executeFilter(null, null, null, this, errorResponse, e, FilterTypes.RESPONSEEXCEPTIONFILTER);
                throw new RESTClientException(errorResponse, e);
            }
        }
    }

    private boolean isStatusCodeAllowed(int statusCode) {
        return (((statusCode >= 200 && statusCode < 300)
                || additionalAllowedStatusCodes.contains(statusCode)
        ) && !additionalDeniedStatusCodes.contains(statusCode));
    }

    protected void executeFilter(HttpResponse httpResponse, HttpRequestBase httpRequest, Response response, Request request, ErrorResponse errorResponse, Exception e, FilterTypes filterType) throws Exception {
        switch (filterType) {
            case REQUESTFILTER:
                for (RequestFilter requestFilter : requestFilters.values()) {
                    requestFilter.filter(request);
                }
                break;
            case REQUESTWRITEFILTER:
                for (RequestWriteFilter requestWriteFilter : requestWriteFilters.values()) {
                    requestWriteFilter.filter(httpRequest, request);
                }
                break;
            case REQUESTEXCEPTIONFILTER:
                for (RequestExceptionFilter requestExceptionFilter : requestExceptionFilters.values()) {
                    requestExceptionFilter.filter(e, httpRequest, request);
                }
                break;
            case RESPONSEFILTER:
                for (ResponseFilter responseFilter : responseFilters.values()) {
                    responseFilter.filter(response, request);
                }
                break;
            case RESPONSEREADFILTER:
                for (ResponseReadFilter responseReadFilter : responseReadFilters.values()) {
                    responseReadFilter.filter(httpResponse, request);
                }
                break;
            case RESPONSEEXCEPTIONFILTER:
                for (ResponseExceptionFilter responseExceptionFilter : responseExceptionFilters.values()) {
                    responseExceptionFilter.filter(e, errorResponse, request);
                }
                break;
            case SUCCESSRESPONSECODEFILTER:
                if (successResponseCodeFilters.isEmpty()) {
                    SuccessResponseCodeFilter successResponseCodeFilter = new DefaultSuccessResponseCodeFilter();
                    successResponseCodeFilter.filter(httpResponse.getStatusLine().getStatusCode(), response);
                } else {
                    for (SuccessResponseCodeFilter successResponseCodeFilter : successResponseCodeFilters.values()) {
                        successResponseCodeFilter.filter(httpResponse.getStatusLine().getStatusCode(), response);
                    }
                }
                break;
            case ERRORRESPONSECODEFILTER:
                if (errorResponseCodeFilters.isEmpty()) {
                    ErrorResponseCodeFilter errorResponseCodeFilter = new DefaultErrorResponseCodeFilter();
                    errorResponseCodeFilter.filter(httpResponse.getStatusLine().getStatusCode(), response, errorResponse);
                } else {
                    for (ErrorResponseCodeFilter errorResponseCodeFilter : errorResponseCodeFilters.values()) {
                        errorResponseCodeFilter.filter(httpResponse.getStatusLine().getStatusCode(), response, errorResponse);
                    }
                }
                break;
        }
    }


    private HashMap<String, String> convertHeaders(Header[] headers) {
        HashMap<String, String> convertedHeaders = new HashMap<>();
        for (Header header : headers) {
            convertedHeaders.put(header.getName(), header.getValue());
        }
        return convertedHeaders;
    }

    public String getEncodedPath() {
        String path = getPath();
        String[] tokens = path.split("/");
        String encodedPath = "";
        URLCodec urlCodec = new URLCodec();
        try {
            for (String token : tokens) {
                if (!token.isEmpty()) {
                    //replace + to %20
                    encodedPath += "/" + urlCodec.encode(token).replace("+", "%20");
                }
            }
        } catch (EncoderException e) {
            log.error("Failed to encode the path properly.", e);
        }
        return encodedPath;
    }

    public Class<T> getParameterClass() {
        Class<T> dataType = (Class<T>) (((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        return dataType;
    }
}
