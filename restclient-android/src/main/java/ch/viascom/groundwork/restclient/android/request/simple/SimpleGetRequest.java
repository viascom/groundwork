package ch.viascom.groundwork.restclient.android.request.simple;

import ch.viascom.groundwork.restclient.android.request.GetRequest;
import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.response.generic.Response;
import lombok.Setter;
import okhttp3.OkHttpClient;

/**
 * @author patrick.boesch@viascom.ch, nikola.stankovic@viascom.ch
 */
public class SimpleGetRequest<T extends Response> extends GetRequest<T> {

    @Setter
    private Class<T> parameterClass;

    public SimpleGetRequest(String url, Class<T> parameterClass) throws RESTClientException {
        this(url, "application/json", new OkHttpClient(), parameterClass);
    }

    public SimpleGetRequest(String url, String mediaType, Class<T> parameterClass) throws RESTClientException {
        this(url, mediaType, new OkHttpClient(), parameterClass);
    }

    public SimpleGetRequest(String url, String mediaType, OkHttpClient httpClient, Class<T> parameterClass) throws RESTClientException {
        this(url, "", mediaType, httpClient, parameterClass);
    }

    public SimpleGetRequest(String url, String path, String mediaType, OkHttpClient httpClient, Class<T> parameterClass) throws RESTClientException {
        super(url, httpClient);
        setPath(path);
        setMediaType(mediaType);
        setParameterClass(parameterClass);
    }

    @Override
    public Class<T> getParameterClass() {
        return parameterClass;
    }
}
