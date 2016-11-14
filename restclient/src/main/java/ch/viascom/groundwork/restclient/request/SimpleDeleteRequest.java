package ch.viascom.groundwork.restclient.request;

import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.response.GenericResponse;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * @author patrick.boesch@viascom.ch
 */
@Getter
@Setter
public class SimpleDeleteRequest extends DeleteRequest<GenericResponse> {
    private String path;
    private String url;

    public SimpleDeleteRequest(String url) throws RESTClientException {
        super(url, HttpClientBuilder.create().build());
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
