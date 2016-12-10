package ch.viascom.groundwork.restclient.android.request.simple;

import ch.viascom.groundwork.restclient.android.request.PutRequest;
import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.response.generic.Response;
import lombok.Setter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * @author patrick.boesch@viascom.ch, nikola.stankovic@viascom.ch
 */
public class SimplePutRequest<T extends Response> extends PutRequest<T> {

    @Setter
    private Class<T> parameterClass;

    public SimplePutRequest(String url, Class<T> parameterClass) throws RESTClientException {
        this(url, "application/json", RequestBody.create(MediaType.parse("application/json"), ""), new OkHttpClient(), parameterClass);
    }

    public SimplePutRequest(String url, RequestBody requestBody, Class<T> parameterClass) throws RESTClientException {
        this(url, "application/json", requestBody, new OkHttpClient(), parameterClass);
    }

    public SimplePutRequest(String url, String mediaType, RequestBody requestBody, Class<T> parameterClass) throws RESTClientException {
        this(url, mediaType, requestBody, new OkHttpClient(), parameterClass);
    }

    public SimplePutRequest(String url, String mediaType, RequestBody requestBody, OkHttpClient httpClient, Class<T> parameterClass) throws RESTClientException {
        this(url, "", mediaType, requestBody, httpClient, parameterClass);
    }

    public SimplePutRequest(String url, String path, String mediaType, RequestBody requestBody, OkHttpClient httpClient, Class<T> parameterClass) throws RESTClientException {
        super(url, httpClient);
        setPath(path);
        setMediaType(mediaType);
        setRequestBody(requestBody);
        setParameterClass(parameterClass);
    }

    @Override
    public Class<T> getParameterClass() {
        return parameterClass;
    }
}
