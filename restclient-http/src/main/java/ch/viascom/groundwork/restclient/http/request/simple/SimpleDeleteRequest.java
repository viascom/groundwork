package ch.viascom.groundwork.restclient.http.request.simple;

import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.http.request.DeleteRequest;
import ch.viascom.groundwork.restclient.response.GenericResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * @author patrick.boesch@viascom.ch
 */
public class SimpleDeleteRequest extends DeleteRequest<GenericResponse> {

    public SimpleDeleteRequest(String url) throws RESTClientException {
        super(url, HttpClientBuilder.create().build());
        setPath("");
    }

    public SimpleDeleteRequest(String url, String mediaType) throws RESTClientException {
        super(url, HttpClientBuilder.create().build());
        setMediaType(mediaType);
        setPath("");
    }

    public SimpleDeleteRequest(HttpClient httpClient, String url) throws RESTClientException {
        super(url, httpClient);
        setUrl(url);
        setPath("");
    }

    public SimpleDeleteRequest(HttpClient httpClient, String url, String path) throws RESTClientException {
        super(url, httpClient);
        setUrl(url);
        setPath(path);
    }
}
