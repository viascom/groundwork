package ch.viascom.groundwork.restclient.request;

import ch.viascom.groundwork.restclient.response.generic.Response;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author patrick.boesch@viascom.ch
 */
public abstract class PostRequest<T extends Response> extends Request<T> {

    private static final Logger log = LogManager.getLogger(PostRequest.class);

    protected PostRequest(String url, HttpClient httpClient) {
        super(url, httpClient);
    }

    @Override
    protected HttpResponse request() throws IOException, URISyntaxException {
        String encodedPath = getEncodedPath();
        log.debug("POST - path: {}", encodedPath);

        HttpPost httpPost = new HttpPost(url + encodedPath);
        httpPost = (HttpPost) prepareQuery(httpPost);
        httpPost = (HttpPost) setRequestHeader(httpPost);
        httpPost.setEntity(getRequestEntity());
        HttpResponse response = httpClient.execute(httpPost, HttpClientContext.create());

        return response;
    }
}
