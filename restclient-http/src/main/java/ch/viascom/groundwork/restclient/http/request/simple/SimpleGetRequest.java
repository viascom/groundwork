package ch.viascom.groundwork.restclient.http.request.simple;

import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.http.request.GetRequest;
import ch.viascom.groundwork.restclient.response.generic.Response;
import lombok.Setter;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * @author patrick.boesch@viascom.ch
 */
public class SimpleGetRequest<T extends Response> extends GetRequest<T> {

    @Setter
    private Class<T> parameterClass;

    public SimpleGetRequest(String url, Class<T> parameterClass) throws RESTClientException {
        this(url, ContentType.APPLICATION_JSON.toString(), HttpClientBuilder.create().build(), parameterClass);
    }

    public SimpleGetRequest(String url, String mediaType, Class<T> parameterClass) throws RESTClientException {
        this(url, mediaType, HttpClientBuilder.create().build(), parameterClass);
    }

    public SimpleGetRequest(String url, String mediaType, HttpClient httpClient, Class<T> parameterClass) throws RESTClientException {
        this(url, "", mediaType, httpClient, parameterClass);
    }

    public SimpleGetRequest(String url, String path, String mediaType, HttpClient httpClient, Class<T> parameterClass) throws RESTClientException {
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
