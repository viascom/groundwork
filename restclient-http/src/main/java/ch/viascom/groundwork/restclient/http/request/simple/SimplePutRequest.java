package ch.viascom.groundwork.restclient.http.request.simple;

import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.http.request.PutRequest;
import ch.viascom.groundwork.restclient.response.GenericResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * @author patrick.boesch@viascom.ch
 */
public class SimplePutRequest extends PutRequest<GenericResponse> {

    public SimplePutRequest(String url) throws RESTClientException {
        super(url, HttpClientBuilder.create().build());
        setPath("");
    }

    public SimplePutRequest(String url, HttpEntity httpEntity) throws RESTClientException {
        super(url, HttpClientBuilder.create().build());
        setPath("");
        setRequestBody(httpEntity);
    }

    public SimplePutRequest(HttpClient httpClient, String url) throws RESTClientException {
        super(url, httpClient);
        setUrl(url);
        setPath("");
    }

    public SimplePutRequest(HttpClient httpClient, String url, String path) throws RESTClientException {
        super(url, httpClient);
        setUrl(url);
        setPath(path);
    }

    public SimplePutRequest(HttpClient httpClient, String url, String path, HttpEntity httpEntity) throws RESTClientException {
        super(url, httpClient);
        setUrl(url);
        setPath(path);
        setRequestBody(httpEntity);
    }
}
