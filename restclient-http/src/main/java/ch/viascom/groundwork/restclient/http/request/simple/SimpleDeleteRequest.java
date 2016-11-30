package ch.viascom.groundwork.restclient.http.request.simple;

import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.http.request.DeleteRequest;
import ch.viascom.groundwork.restclient.response.generic.Response;
import lombok.Setter;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * @author patrick.boesch@viascom.ch
 */
public class SimpleDeleteRequest<T extends Response> extends DeleteRequest<T> {

    @Setter
    private Class<T> parameterClass;

    public SimpleDeleteRequest(String url, Class<T> parameterClass) throws RESTClientException {
        this(url, ContentType.APPLICATION_JSON.toString(), HttpClientBuilder.create().build(), parameterClass);
    }

    public SimpleDeleteRequest(String url, String mediaType, Class<T> parameterClass) throws RESTClientException {
        this(url, mediaType, HttpClientBuilder.create().build(), parameterClass);
    }

    public SimpleDeleteRequest(String url, String mediaType, HttpClient httpClient, Class<T> parameterClass) throws RESTClientException {
        this(url, "", mediaType, httpClient, parameterClass);
    }

    public SimpleDeleteRequest(String url, String path, String mediaType, HttpClient httpClient, Class<T> parameterClass) throws RESTClientException {
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
