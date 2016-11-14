package ch.viascom.groundwork.restclient.request;

import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.response.generic.Response;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface RequestInterface<T extends Response> {

    /*
    protected String url;
    protected String path;
    protected URI requestUrl;
    protected HttpEntity requestBody;
    protected String mediaType;
    protected HttpClient httpClient;
    protected HashMap<String, String> queryParamMap = new HashMap<>();
    protected HashMap<String, String> headerMap = new HashMap<>();
     */

    String getPath();
    void setPath(String path);

    String getUrl();
    void setUrl(String url);

    String getMediaType();
    void setMediaType(String mediaType);

    HashMap<String, String> getQueryParamMap();
    void setQueryParamMap(HashMap<String, String> queryParamMap);

    HashMap<String, String> getHeaderMap();
    void setHeaderMap(HashMap<String, String> headerMap);

    Object request() throws IOException, URISyntaxException;

    void setRequestHeaders(HashMap<String, String> headerMap);
    void addRequestHeader(String name, String value);
    void setRequestParams(ArrayList<String> params, Object o) throws RESTClientException;
    void prepareQuery() throws URISyntaxException;

    T execute() throws RESTClientException, Exception;

    String getEncodedPath();
    Class<T> getParameterClass();
}
