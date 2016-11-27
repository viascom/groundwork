package ch.viascom.groundwork.restclient.http.request;

import ch.viascom.groundwork.restclient.exception.AccessRESTClientException;
import ch.viascom.groundwork.restclient.exception.AuthorizationRESTClientException;
import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.request.RequestInterface;
import ch.viascom.groundwork.restclient.response.GenericResponse;
import ch.viascom.groundwork.restclient.response.JSONResponse;
import ch.viascom.groundwork.restclient.response.generic.ErrorResponse;
import ch.viascom.groundwork.restclient.response.generic.Response;
import ch.viascom.groundwork.restclient.response.generic.ResponseHeader;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
public abstract class Request<T extends Response> implements RequestInterface<T> {

    private static final Logger log = LogManager.getLogger(Request.class);

    protected String url;
    protected String path;
    protected URI requestUrl;
    protected String mediaType;
    protected HttpEntity requestBody = new BasicHttpEntity();
    protected HttpClient httpClient;
    protected HashMap<String, String> queryParamMap = new HashMap<>();
    protected HashMap<String, String> headerMap = new HashMap<>();

    protected Request(String url, HttpClient httpClient) {
        this.httpClient = httpClient;
        this.url = url;
    }

    public abstract Object request() throws IOException, URISyntaxException;

    public void setHeaderMap(HashMap<String, String> headerMap) {
        this.headerMap = headerMap;
    }

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

    public void addQueryParam(String name, String value){
        queryParamMap.put(name,value);
    }

    public HttpEntity getRequestBody() {
        return this.requestBody;
    }

    public void setRequestBody(HttpEntity requestBody) {
        this.requestBody = requestBody;
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

    public T execute() throws RESTClientException {
        Response output;
        String responseBody = "";
        ResponseHeader responseHeader = new ResponseHeader();
        try {
            HttpResponse response = (HttpResponse) request();
            int status = response.getStatusLine().getStatusCode();
            responseHeader.setResponseHeaders(convertHeaders(response.getAllHeaders()));
            responseHeader.setStatusCode(status);
            responseHeader.setRequestPath(url + getEncodedPath());
            HttpEntity entity = response.getEntity();

            if (status >= 200 && status < 300) {
                log.debug("-> Response status: " + status);

                if (entity != null) {
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
                return (T) output;
            } else {
                log.debug("-> Invalid response status: " + status);
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
