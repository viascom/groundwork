package ch.viascom.groundwork.restclient.android.request;

import ch.viascom.groundwork.restclient.filter.FilterTypes;
import ch.viascom.groundwork.restclient.request.PostRequestInterface;
import ch.viascom.groundwork.restclient.response.generic.Response;
import okhttp3.OkHttpClient;


/**
 * @author patrick.boesch@viascom.ch, nikola.stankovic@viascom.ch
 */
public abstract class PostRequest<T extends Response> extends Request<T> implements PostRequestInterface<T> {

    protected PostRequest(String url, OkHttpClient httpClient) {
        super(url, httpClient);
    }

    @Override
    public okhttp3.Response request() throws Exception {

        executeFilter(null, null, null, this, null, null, FilterTypes.REQUESTFILTER);
        String encodedPath = getEncodedPath();

        okhttp3.Request.Builder httpPostBuilder = new okhttp3.Request.Builder();

        try {
            prepareQuery();
            httpPostBuilder.url(requestUrl.toURL());
            httpPostBuilder.headers(getRequestHeader());
            httpPostBuilder.post(getRequestBody());
            okhttp3.Request httpPost = httpPostBuilder.build();
            executeFilter(null, httpPost, null, this, null, null, FilterTypes.REQUESTWRITEFILTER);
            okhttp3.Response response = httpClient.newCall(httpPost).execute();

            return response;

        } catch (Exception e) {
            okhttp3.Request httpPost = httpPostBuilder.build();
            executeFilter(null, httpPost, null, this, null, e, FilterTypes.REQUESTEXCEPTIONFILTER);
        }

        return null;
    }
}
