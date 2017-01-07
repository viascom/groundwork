package ch.viascom.groundwork.restclient.android.request;

import ch.viascom.groundwork.restclient.filter.FilterTypes;
import ch.viascom.groundwork.restclient.request.GetRequestInterface;
import ch.viascom.groundwork.restclient.response.generic.Response;
import okhttp3.OkHttpClient;

/**
 * @author patrick.boesch@viascom.ch, nikola.stankovic@viascom.ch
 */
public abstract class GetRequest<T extends Response> extends Request<T> implements GetRequestInterface<T> {

    protected GetRequest(String url, OkHttpClient httpClient) {
        super(url, httpClient);
    }

    @Override
    public okhttp3.Response request() throws Exception {

        executeFilter(null, null, null, this, null, null, FilterTypes.REQUESTFILTER);
        String encodedPath = getEncodedPath();

        okhttp3.Request.Builder httpGetBuilder = new okhttp3.Request.Builder();

        try {
            prepareQuery();
            httpGetBuilder.url(requestUrl.toURL());
            httpGetBuilder.headers(getRequestHeader());
            okhttp3.Request httpGet = httpGetBuilder.build();
            executeFilter(null, httpGet, null, this, null, null, FilterTypes.REQUESTWRITEFILTER);
            okhttp3.Response response = httpClient.newCall(httpGet).execute();

            return response;

        } catch (Exception e) {
            okhttp3.Request httpGet = httpGetBuilder.build();
            executeFilter(null, httpGet, null, this, null, e, FilterTypes.REQUESTEXCEPTIONFILTER);
        }

        return null;
    }
}
