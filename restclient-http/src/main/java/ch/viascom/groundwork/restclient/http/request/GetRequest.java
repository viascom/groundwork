package ch.viascom.groundwork.restclient.http.request;

import ch.viascom.groundwork.restclient.filter.FilterTypes;
import ch.viascom.groundwork.restclient.request.GetRequestInterface;
import ch.viascom.groundwork.restclient.response.generic.Response;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author patrick.boesch@viascom.ch
 */
public abstract class GetRequest<T extends Response> extends Request<T> implements GetRequestInterface<T> {
    private static final Logger log = LogManager.getLogger(PostRequest.class);

    protected GetRequest(String url, HttpClient httpClient) {
        super(url, httpClient);
    }

    @Override
    public HttpResponse request() throws Exception {
        executeFilter(null, null, null, this, null, null, FilterTypes.REQUESTFILTER);
        String encodedPath = getEncodedPath();
        log.debug("GET - path: {}", encodedPath);

        HttpGet httpGet = new HttpGet();

        try {
            prepareQuery();
            httpGet.setURI(requestUrl);
            httpGet.setHeaders(getRequestHeader());
            executeFilter(null, httpGet, null, this, null, null, FilterTypes.REQUESTWRITEFILTER);
            HttpResponse response = httpClient.execute(httpGet, httpClientContext);

            return response;

        } catch (Exception e) {
            executeFilter(null, httpGet, null, this, null, e, FilterTypes.REQUESTEXCEPTIONFILTER);
        }

        return null;
    }
}
