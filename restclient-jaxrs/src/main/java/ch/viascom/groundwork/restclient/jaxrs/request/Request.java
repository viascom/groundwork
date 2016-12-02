package ch.viascom.groundwork.restclient.jaxrs.request;

import ch.viascom.groundwork.restclient.exception.AccessRESTClientException;
import ch.viascom.groundwork.restclient.exception.AuthorizationRESTClientException;
import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.request.RequestInterface;
import ch.viascom.groundwork.restclient.response.GenericResponse;
import ch.viascom.groundwork.restclient.response.generic.ErrorResponse;
import ch.viascom.groundwork.restclient.response.generic.ResponseHeader;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.*;
import java.io.IOException;
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
public abstract class Request<T extends ch.viascom.groundwork.restclient.response.generic.Response> implements RequestInterface<T> {

    private static final Logger log = LogManager.getLogger(Request.class);

    protected String url;
    protected String path;
    protected URI requestUrl;
    protected String mediaType;
    protected Object requestBody;
    protected Client httpClient;
    protected Class responseClass = String.class;
    protected HashMap<String, String> queryParamMap = new HashMap<>();
    protected HashMap<String, String> headerMap = new HashMap<>();

    protected Request(String url, Client httpClient) {
        this.httpClient = httpClient;
        this.url = url;
    }

    public abstract Object request() throws IOException, URISyntaxException;

    public void setRequestHeaders(HashMap<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public void addRequestHeader(String name, String value) {
        this.headerMap.put(name, value);
    }

    public MultivaluedMap<String, Object> getRequestHeader() {
        MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();

        for (String key : headerMap.keySet()) {
            headers.put(key, headers.get(key));
        }
        return headers;
    }

    public void setRequestParams(ArrayList<String> params, Object o) throws RESTClientException {
        try {
            Class clazz = o.getClass();

            HashMap<String, String> paramMap = new HashMap<>();

            for (String param : params) {
                Field field = clazz.getDeclaredField(param);

                field.setAccessible(true);

                String paramName = field.getName();

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

    public Object getRequestBody() {
        return this.requestBody;
    }

    public void setRequestBody(Object requestBody) {
        this.requestBody = requestBody;
    }

    public void prepareQuery() throws URISyntaxException {
        UriBuilder uriBuilder = UriBuilder.fromUri(url + getEncodedPath());

        HashMap<String, String> param = queryParamMap;
        if (param != null && !param.isEmpty()) {
            for (String key : param.keySet()) {
                uriBuilder.queryParam(key, param.get(key).toString());
            }
        }
        this.requestUrl = uriBuilder.build();
    }

    public T execute() throws RESTClientException, Exception {
        ch.viascom.groundwork.restclient.response.generic.Response output;
        String responseBody = "";
        ResponseHeader responseHeader = new ResponseHeader();
        try {
            Response response = (Response) request();
            int status = response.getStatus();
            responseHeader.setResponseHeaders(convertHeaders(response.getHeaders()));
            responseHeader.setStatusCode(status);
            responseHeader.setRequestPath(url + getEncodedPath());

            if (status >= 200 && status < 300) {
                log.debug("-> Response status: " + status);

                if (response.hasEntity()) {
                    if (getParameterClass() == GenericResponse.class) {
                        output = new GenericResponse(responseHeader, response.readEntity(responseClass));
                    } else {
                        output = response.readEntity(getParameterClass());
                    }
                } else {
                    //NoContentResponse
                    output = getParameterClass().newInstance();
                }
                output.setResponseHeader(responseHeader);
                return (T) output;
            } else {
                log.error("-> Invalid response status: " + status);
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setRequestBody(getRequestBody().toString());
                errorResponse.setResponseBody(responseBody);
                errorResponse.setResponseHeader(responseHeader);
                switch (status) {
                    case 401:
                        throw new AuthorizationRESTClientException(errorResponse, "Response-Statuscode: " + String.valueOf(status));
                    case 403:
                        throw new AccessRESTClientException(errorResponse, "Response-Statuscode: " + String.valueOf(status));
                    default:
                        throw new RESTClientException(errorResponse, "Response-Statuscode: " + String.valueOf(status));
                }

            }
        } catch (Exception e) {
            if (e instanceof RESTClientException) {
                //Pass through APIExceptions
                throw (RESTClientException) e;
            } else {
                log.error("RESTClient-Error - " + e.toString());
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.setErrorMessage(e.getMessage());
                errorResponse.setResponseHeader(responseHeader);
                throw new RESTClientException(errorResponse, e.toString());
            }
        }
    }

    private HashMap<String, String> convertHeaders(MultivaluedMap<String, Object> headers) {
        HashMap<String, String> convertedHeaders = new HashMap<>();
        for (String key : headers.keySet()) {
            convertedHeaders.put(key, headers.get(key).toString());
        }
        return convertedHeaders;
    }

    public String getEncodedPath() {
        return path;
    }

    public Class<T> getParameterClass() {
        Class<T> dataType = (Class<T>) (((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        return dataType;
    }
}
