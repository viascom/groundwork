package ch.viascom.groundwork.restclient.jaxrs.request.simple;

import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.jaxrs.request.DeleteRequest;
import ch.viascom.groundwork.restclient.response.GenericResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * @author patrick.boesch@viascom.ch
 */
public class SimpleDeleteRequest extends DeleteRequest<GenericResponse> {

    public SimpleDeleteRequest(String url) throws RESTClientException {
        super(url, ClientBuilder.newClient());
        setPath("");
    }

    public SimpleDeleteRequest(Client httpClient, String url) throws RESTClientException {
        super(url, httpClient);
        setUrl(url);
        setPath("");
    }

    public SimpleDeleteRequest(Client httpClient, String url, String path) throws RESTClientException {
        super(url, httpClient);
        setUrl(url);
        setPath(path);
    }
}
