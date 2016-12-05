package ch.viascom.groundwork.restclient.jaxrs.request;

import ch.viascom.groundwork.restclient.request.PutRequestInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author patrick.boesch@viascom.ch
 */
public abstract class PutRequest<T extends ch.viascom.groundwork.restclient.response.generic.Response> extends Request<T> implements PutRequestInterface<T> {
    private static final Logger log = LogManager.getLogger(PostRequest.class);

    protected PutRequest(String url, Client httpClient) {
        super(url, httpClient);
    }

    @Override
    public Response request() throws IOException, URISyntaxException {
        String encodedPath = getEncodedPath();
        log.debug("PUT - path: {}", encodedPath);

        prepareQuery();

        WebTarget webTarget = getHttpClient().target(requestUrl);

        Invocation.Builder invocationBuilder =  webTarget.request(getMediaType());
        invocationBuilder.headers(getRequestHeader());
        Response response = invocationBuilder.put(Entity.entity(getRequestBody(), getMediaType()));

        return response;
    }
}
