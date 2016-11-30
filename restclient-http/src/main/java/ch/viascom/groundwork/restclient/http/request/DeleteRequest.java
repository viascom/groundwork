package ch.viascom.groundwork.restclient.http.request;

import ch.viascom.groundwork.restclient.filter.FilterTypes;
import ch.viascom.groundwork.restclient.request.DeleteRequestInterface;
import ch.viascom.groundwork.restclient.response.generic.Response;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author patrick.boesch@viascom.ch
 */
public abstract class DeleteRequest<T extends Response> extends Request<T> implements DeleteRequestInterface<T> {

    private static final Logger log = LogManager.getLogger(PostRequest.class);

    protected DeleteRequest(String url, HttpClient httpClient) {
        super(url, httpClient);
    }

    @Override
    public HttpResponse request() throws Exception {
        executeFilter(null, null, null, this, null, null, FilterTypes.REQUESTFILTER);
        String encodedPath = getEncodedPath();
        log.debug("DELETE - path: {}", encodedPath);

        HttpDelete httpDelete = new HttpDelete();
        try {
            prepareQuery();
            httpDelete.setURI(requestUrl);
            httpDelete.setHeaders(getRequestHeader());
            executeFilter(null, httpDelete, null, this, null, null, FilterTypes.REQUESTWRITEFILTER);
            HttpResponse response = httpClient.execute(httpDelete, httpClientContext);

            return response;
        } catch (Exception e) {
            executeFilter(null, httpDelete, null, this, null, e, FilterTypes.REQUESTEXCEPTIONFILTER);
        }
        return null;
    }
}
