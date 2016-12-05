package ch.viascom.groundwork.restclient.jaxrs.request;

import ch.viascom.groundwork.restclient.request.PostRequestInterface;
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
public abstract class PostRequest<T extends ch.viascom.groundwork.restclient.response.generic.Response> extends Request<T> implements PostRequestInterface<T> {

    private static final Logger log = LogManager.getLogger(PostRequest.class);

    protected PostRequest(String url, Client httpClient) {
        super(url, httpClient);
    }

    @Override
    public Response request() throws IOException, URISyntaxException {
        String encodedPath = getEncodedPath();
        log.debug("POST - path: {}", encodedPath);

        prepareQuery();

        WebTarget webTarget = getHttpClient().target(requestUrl);

        Invocation.Builder invocationBuilder =  webTarget.request(getMediaType());
        invocationBuilder.headers(getRequestHeader());
        Response response = invocationBuilder.post(Entity.entity(getRequestBody(), getMediaType()));

        return response;
    }
}
