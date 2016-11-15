package ch.viascom.groundwork.restclient.jaxrs.request.simple;

import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.jaxrs.request.PostRequest;
import ch.viascom.groundwork.restclient.response.GenericResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * @author patrick.boesch@viascom.ch
 */
public class SimplePostRequest extends PostRequest<GenericResponse> {

    public SimplePostRequest(String url) throws RESTClientException {
        super(url, ClientBuilder.newClient());
        setPath("");
    }

    public SimplePostRequest(String url, Client httpEntity) throws RESTClientException {
        super(url, ClientBuilder.newClient());
        setPath("");
        setRequestBody(httpEntity);
    }

    public SimplePostRequest(Client httpClient, String url) throws RESTClientException {
        super(url, httpClient);
        setUrl(url);
        setPath("");
    }

    public SimplePostRequest(Client httpClient, String url, String path) throws RESTClientException {
        super(url, httpClient);
        setUrl(url);
        setPath(path);
    }

    public SimplePostRequest(Client httpClient, String url, String path, Object httpEntity) throws RESTClientException {
        super(url, httpClient);
        setUrl(url);
        setPath(path);
        setRequestBody(httpEntity);
    }
}