package ch.viascom.groundwork.restclient.android.request;

import ch.viascom.groundwork.restclient.filter.FilterTypes;
import ch.viascom.groundwork.restclient.request.DeleteRequestInterface;
import ch.viascom.groundwork.restclient.response.generic.Response;
import okhttp3.OkHttpClient;

/**
 * @author patrick.boesch@viascom.ch, nikola.stankovic@viascom.ch
 */
public abstract class DeleteRequest<T extends Response> extends Request<T> implements DeleteRequestInterface<T> {

    protected DeleteRequest(String url, OkHttpClient httpClient) {
        super(url, httpClient);
    }

    @Override
    public okhttp3.Response request() throws Exception {

        executeFilter(null, null, null, this, null, null, FilterTypes.REQUESTFILTER);
        String encodedPath = getEncodedPath();

        okhttp3.Request.Builder httpDeleteBuilder = new okhttp3.Request.Builder();

        try {
            prepareQuery();
            httpDeleteBuilder.url(requestUrl.toURL());
            httpDeleteBuilder.headers(getRequestHeader());
            httpDeleteBuilder.delete();
            okhttp3.Request httpDelete = httpDeleteBuilder.build();
            executeFilter(null, httpDelete, null, this, null, null, FilterTypes.REQUESTWRITEFILTER);
            okhttp3.Response response = httpClient.newCall(httpDelete).execute();

            return response;

        } catch (Exception e) {
            okhttp3.Request httpDelete = httpDeleteBuilder.build();
            executeFilter(null, httpDelete, null, this, null, e, FilterTypes.REQUESTEXCEPTIONFILTER);
        }

        return null;
    }
}
