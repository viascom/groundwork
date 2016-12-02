package ch.viascom.groundwork.restclient.jaxrs.request.simple;

import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.jaxrs.request.GetRequest;
import ch.viascom.groundwork.restclient.response.GenericResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 * @author patrick.boesch@viascom.ch
 */
public class SimpleGetRequest extends GetRequest<GenericResponse> {

    public SimpleGetRequest(String url) throws RESTClientException {
        super(url, ClientBuilder.newClient());
        setPath("");
    }

    public SimpleGetRequest(Client httpClient, String url) throws RESTClientException {
        super(url, httpClient);
        setUrl(url);
        setPath("");
    }

    public SimpleGetRequest(Client httpClient, String url, String path) throws RESTClientException {
        super(url, httpClient);
        setUrl(url);
        setPath(path);
    }
}
