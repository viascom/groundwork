package ch.viascom.groundwork.restclient.jaxrs.request.simple;

import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.jaxrs.request.PutRequest;
import ch.viascom.groundwork.restclient.response.GenericResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * @author patrick.boesch@viascom.ch
 */
public class SimplePutRequest extends PutRequest<GenericResponse> {

    public SimplePutRequest(String url) throws RESTClientException {
        super(url, ClientBuilder.newClient());
        setPath("");
    }

    public SimplePutRequest(String url, Client httpEntity) throws RESTClientException {
        super(url, ClientBuilder.newClient());
        setPath("");
        setRequestBody(httpEntity);
    }

    public SimplePutRequest(Client httpClient, String url) throws RESTClientException {
        super(url, httpClient);
        setUrl(url);
        setPath("");
    }

    public SimplePutRequest(Client httpClient, String url, String path) throws RESTClientException {
        super(url, httpClient);
        setUrl(url);
        setPath(path);
    }

    public SimplePutRequest(Client httpClient, String url, String path, Object httpEntity) throws RESTClientException {
        super(url, httpClient);
        setUrl(url);
        setPath(path);
        setRequestBody(httpEntity);
    }
}
