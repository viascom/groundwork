package ch.viascom.groundwork.restclient.android.request;

import ch.viascom.groundwork.restclient.filter.FilterTypes;
import ch.viascom.groundwork.restclient.request.PutRequestInterface;
import ch.viascom.groundwork.restclient.response.generic.Response;
import okhttp3.OkHttpClient;

/**
 * @author patrick.boesch@viascom.ch, nikola.stankovic@viascom.ch
 */
public abstract class PutRequest<T extends Response> extends Request<T> implements PutRequestInterface<T> {

    protected PutRequest(String url, OkHttpClient httpClient) {
        super(url, httpClient);
    }

    @Override
    public okhttp3.Response request() throws Exception {

        executeFilter(null, null, null, this, null, null, FilterTypes.REQUESTFILTER);
        String encodedPath = getEncodedPath();

        okhttp3.Request.Builder httpPutBuilder = new okhttp3.Request.Builder();

        try {
            prepareQuery();
            httpPutBuilder.url(requestUrl.toURL());
            httpPutBuilder.headers(getRequestHeader());
            httpPutBuilder.put(getRequestBody());
            okhttp3.Request httpPut = httpPutBuilder.build();
            executeFilter(null, httpPut, null, this, null, null, FilterTypes.REQUESTWRITEFILTER);
            okhttp3.Response response = httpClient.newCall(httpPut).execute();

            return response;

        } catch (Exception e) {
            okhttp3.Request httpPut = httpPutBuilder.build();
            executeFilter(null, httpPut, null, this, null, e, FilterTypes.REQUESTEXCEPTIONFILTER);
        }

        return null;

    }
}
