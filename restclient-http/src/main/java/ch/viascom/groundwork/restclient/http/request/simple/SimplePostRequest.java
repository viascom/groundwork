package ch.viascom.groundwork.restclient.http.request.simple;

import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.http.request.PostRequest;
import ch.viascom.groundwork.restclient.response.GenericResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * @author patrick.boesch@viascom.ch
 */
public class SimplePostRequest extends PostRequest<GenericResponse> {

    public SimplePostRequest(String url) throws RESTClientException {
        super(url, HttpClientBuilder.create().build());
        setPath("");
    }

    public SimplePostRequest(String url, String mediaType) throws RESTClientException {
        super(url, HttpClientBuilder.create().build());
        setMediaType(mediaType);
        setPath("");
    }

    public SimplePostRequest(String url, HttpEntity httpEntity) throws RESTClientException {
        super(url, HttpClientBuilder.create().build());
        setPath("");
        setRequestBody(httpEntity);
    }

    public SimplePostRequest(HttpClient httpClient, String url) throws RESTClientException {
        super(url, httpClient);
        setUrl(url);
        setPath("");
    }

    public SimplePostRequest(HttpClient httpClient, String url, String path) throws RESTClientException {
        super(url, httpClient);
        setUrl(url);
        setPath(path);
    }

    public SimplePostRequest(HttpClient httpClient, String url, String path, HttpEntity httpEntity) throws RESTClientException {
        super(url, httpClient);
        setUrl(url);
        setPath(path);
        setRequestBody(httpEntity);
    }
}