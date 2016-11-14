package ch.viascom.groundwork.restclient.request;

import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.response.GenericResponse;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * @author patrick.boesch@viascom.ch
 */
@Getter
@Setter
public class SimplePostRequest extends PostRequest<GenericResponse> {

    private String path;
    private String url;
    private HttpEntity requestEntity;

    public SimplePostRequest(String url) throws RESTClientException {
        super(url, HttpClientBuilder.create().build());
        setPath("");
    }

    public SimplePostRequest(String url, HttpEntity httpEntity) throws RESTClientException {
        super(url, HttpClientBuilder.create().build());
        setPath("");
        setRequestEntity(httpEntity);
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
        setRequestEntity(httpEntity);
    }
}