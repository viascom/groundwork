package ch.viascom.groundwork.restclient.android.request;


import ch.viascom.groundwork.restclient.android.request.filter.request.RequestExceptionFilter;
import ch.viascom.groundwork.restclient.android.request.filter.request.RequestFilter;
import ch.viascom.groundwork.restclient.android.request.filter.request.RequestWriteFilter;
import ch.viascom.groundwork.restclient.android.request.filter.response.ResponseExceptionFilter;
import ch.viascom.groundwork.restclient.android.request.filter.response.ResponseFilter;
import ch.viascom.groundwork.restclient.android.request.filter.response.ResponseReadFilter;
import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.filter.FilterTypes;
import ch.viascom.groundwork.restclient.filter.RESTFilter;
import ch.viascom.groundwork.restclient.filter.response.ErrorResponseCodeFilter;
import ch.viascom.groundwork.restclient.filter.response.SuccessResponseCodeFilter;
import ch.viascom.groundwork.restclient.filter.response.defaults.DefaultErrorResponseCodeFilter;
import ch.viascom.groundwork.restclient.filter.response.defaults.DefaultSuccessResponseCodeFilter;
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
import okhttp3.*;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author patrick.boesch@viascom.ch, nikola.stankovic@viascom.ch
 */
@Data
public abstract class Request<T extends Response> implements RequestInterface<T> {

    //Request data
    protected String url = "";
    protected String path = "";
    protected URI requestUrl;
    protected String mediaType = "application/json";
    protected RequestBody requestBody = RequestBody.create(MediaType.parse(mediaType), "");
    protected OkHttpClient httpClient = new OkHttpClient();
    //    protected HttpClientContext httpClientContext = HttpClientContext.create();
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


    protected Request(String url, OkHttpClient httpClient) {
        this.httpClient = httpClient;
        this.url = url;
    }

    public abstract Object request() throws Exception;

    public void addHeaders(String name, String value) {
        this.headerMap.put(name, value);
    }

    public Headers getRequestHeader() {
        return Headers.of(headerMap);
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
            restFilters.addAll(responseExceptionFilters.values());
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

        //HttpUrl.Builder builder = new HttpUrl.Builder();
        String query = "";
        try {
            HashMap<String, String> param = queryParamMap;
            if (param != null && !param.isEmpty()) {
                query = "?";
                for (String key : param.keySet()) {
                    query += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(param.get(key).toString(), "UTF-8") + "&";
                }
                query =  query.substring(0, query.length() - 1);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        URI uri = new URI(url + getEncodedPath() + query);

        //builder.scheme("");
        //builder.host("");
        this.requestUrl = uri;
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
            okhttp3.Response response = (okhttp3.Response) request();
            executeFilter(response, null, null, this, null, null, FilterTypes.RESPONSEREADFILTER);
            int status = response.code();
            responseHeader.setResponseHeaders(convertHeaders(response.headers()));
            responseHeader.setStatusCode(status);
            responseHeader.setRequestPath(url + getEncodedPath());
            ResponseBody entity = response.body();

            if (isStatusCodeAllowed(status)) {

                if (entity != null && getParameterClass() != NoContentResponse.class) {
                    if (getParameterClass() == GenericResponse.class) {
                        output = new GenericResponse(entity);
                    } else if (getParameterClass() == JSONResponse.class) {
                        output = new JSONResponse(entity.string());
                    } else {
                        responseBody = entity.string();
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

    protected void executeFilter(okhttp3.Response httpResponse, okhttp3.Request httpRequest, Response response,
                                 Request request, ErrorResponse errorResponse, Exception e, FilterTypes filterType) throws Exception {

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
                    successResponseCodeFilter.filter(httpResponse.code(), response);
                } else {
                    for (SuccessResponseCodeFilter successResponseCodeFilter : successResponseCodeFilters.values()) {
                        successResponseCodeFilter.filter(httpResponse.code(), response);
                    }
                }
                break;
            case ERRORRESPONSECODEFILTER:
                if (errorResponseCodeFilters.isEmpty()) {
                    ErrorResponseCodeFilter errorResponseCodeFilter = new DefaultErrorResponseCodeFilter();
                    errorResponseCodeFilter.filter(httpResponse.code(), response, errorResponse);
                } else {
                    for (ErrorResponseCodeFilter errorResponseCodeFilter : errorResponseCodeFilters.values()) {
                        errorResponseCodeFilter.filter(httpResponse.code(), response, errorResponse);
                    }
                }
                break;
        }
    }


    private HashMap<String, String> convertHeaders(Headers headers) {

        HashMap<String, String> convertedHeaders = new HashMap<>();

        for (String name : headers.names()) {
            convertedHeaders.put(name, headers.get(name));
        }

        return convertedHeaders;
    }

    public String getEncodedPath() {
        String path = getPath();
        String[] tokens = path.split("/");
        String encodedPath = "";

        try {
            for (String token : tokens) {
                if (!token.isEmpty()) {
                    //replace + to %20
                    encodedPath += "/" + URLEncoder.encode(token, "UTF-8").replace("+", "%20");
                }
            }
        } catch (UnsupportedEncodingException e) {
            // if your system does not support utf-8 it's your problem ;)
        }

        return encodedPath;
    }

    public Class<T> getParameterClass() {
        Class<T> dataType = (Class<T>) (((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        return dataType;
    }
}
