package ch.viascom.groundwork.restclient.http.request;

import ch.viascom.groundwork.restclient.filter.FilterTypes;
import ch.viascom.groundwork.restclient.request.PostRequestInterface;
import ch.viascom.groundwork.restclient.response.generic.Response;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author patrick.boesch@viascom.ch
 */
public abstract class PostRequest<T extends Response> extends Request<T> implements PostRequestInterface<T> {

    private static final Logger log = LogManager.getLogger(PostRequest.class);

    protected PostRequest(String url, HttpClient httpClient) {
        super(url, httpClient);
    }

    @Override
    public HttpResponse request() throws Exception {
        executeFilter(null, null, null, this, null, null, FilterTypes.REQUESTFILTER);
        String encodedPath = getEncodedPath();
        log.debug("POST - path: {}", encodedPath);

        HttpPost httpPost = new HttpPost();
        try {
            prepareQuery();
            httpPost.setURI(requestUrl);
            httpPost.setEntity(getRequestBody());
            httpPost.setHeaders(getRequestHeader());
            executeFilter(null, httpPost, null, this, null, null, FilterTypes.REQUESTWRITEFILTER);
            HttpResponse response = httpClient.execute(httpPost, httpClientContext);

            return response;
        } catch (Exception e) {
            executeFilter(null, httpPost, null, this, null, e, FilterTypes.REQUESTEXCEPTIONFILTER);
        }
        return null;
    }
}
