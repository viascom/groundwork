package ch.viascom.groundwork.restclient.http.request;

import ch.viascom.groundwork.restclient.request.GetRequestInterface;
import ch.viascom.groundwork.restclient.response.generic.Response;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author patrick.boesch@viascom.ch
 */
public abstract class GetRequest<T extends Response> extends Request<T> implements GetRequestInterface<T> {
    private static final Logger log = LogManager.getLogger(PostRequest.class);

    protected GetRequest(String url, HttpClient httpClient) {
        super(url, httpClient);
    }

    @Override
    public HttpResponse request() throws IOException, URISyntaxException {
        String encodedPath = getEncodedPath();
        log.debug("GET - path: {}", encodedPath);

        prepareQuery();
        HttpGet httpGet = new HttpGet(requestUrl);
        httpGet.setHeaders(getRequestHeader());
        HttpResponse response = httpClient.execute(httpGet, HttpClientContext.create());

        return response;
    }
}
