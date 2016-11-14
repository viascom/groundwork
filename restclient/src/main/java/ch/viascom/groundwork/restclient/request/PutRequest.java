package ch.viascom.groundwork.restclient.request;

import ch.viascom.groundwork.restclient.response.generic.Response;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author patrick.boesch@viascom.ch
 */
public abstract class PutRequest<T extends Response> extends Request<T> {
    private static final Logger log = LogManager.getLogger(PostRequest.class);

    protected PutRequest(String url, HttpClient httpClient) {
        super(url, httpClient);
    }

    @Override
    protected HttpResponse request() throws IOException, URISyntaxException {
        String encodedPath = getEncodedPath();
        log.debug("PUT - path: {}", encodedPath);

        HttpPut httpPut = new HttpPut(url + encodedPath);
        httpPut = (HttpPut) prepareQuery(httpPut);
        httpPut = (HttpPut) setRequestHeader(httpPut);
        httpPut.setEntity(getRequestEntity());
        HttpResponse response = httpClient.execute(httpPut, HttpClientContext.create());

        return response;
    }
}
