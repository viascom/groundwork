package ch.viascom.groundwork.restclient.request;

import ch.viascom.groundwork.restclient.response.generic.Response;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author patrick.boesch@viascom.ch
 */
public abstract class DeleteRequest<T extends Response> extends Request<T> {

    private static final Logger log = LogManager.getLogger(PostRequest.class);

    protected DeleteRequest(String url, HttpClient httpClient) {
        super(url, httpClient);
    }

    protected HttpEntity getRequestEntity() {
        return null;
    }

    @Override
    protected HttpResponse request() throws IOException, URISyntaxException {
        String encodedPath = getEncodedPath();
        log.debug("DELETE - path: {}", encodedPath);

        HttpDelete httpDelete = new HttpDelete(url + encodedPath);
        httpDelete = (HttpDelete) prepareQuery(httpDelete);
        httpDelete = (HttpDelete) setRequestHeader(httpDelete);
        HttpResponse response = httpClient.execute(httpDelete, HttpClientContext.create());

        return response;
    }
}
