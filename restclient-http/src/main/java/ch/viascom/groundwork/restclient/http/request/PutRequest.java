package ch.viascom.groundwork.restclient.http.request;

import ch.viascom.groundwork.restclient.filter.FilterTypes;
import ch.viascom.groundwork.restclient.request.PutRequestInterface;
import ch.viascom.groundwork.restclient.response.generic.Response;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author patrick.boesch@viascom.ch
 */
public abstract class PutRequest<T extends Response> extends Request<T> implements PutRequestInterface<T> {
    private static final Logger log = LogManager.getLogger(PostRequest.class);

    protected PutRequest(String url, HttpClient httpClient) {
        super(url, httpClient);
    }

    @Override
    public HttpResponse request() throws Exception {
        executeFilter(null, null, null, this, null, null, FilterTypes.REQUESTFILTER);
        String encodedPath = getEncodedPath();
        log.debug("PUT - path: {}", encodedPath);

        HttpPut httpPut = new HttpPut();
        try {
            prepareQuery();
            httpPut.setURI(requestUrl);
            httpPut.setHeaders(getRequestHeader());
            httpPut.setEntity(getRequestBody());
            executeFilter(null, httpPut, null, this, null, null, FilterTypes.REQUESTWRITEFILTER);
            HttpResponse response = httpClient.execute(httpPut, httpClientContext);

            return response;
        } catch (Exception e) {
            executeFilter(null, httpPut, null, this, null, e, FilterTypes.REQUESTEXCEPTIONFILTER);
        }
        return null;

    }
}
