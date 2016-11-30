package ch.viascom.groundwork.restclient.http.request.simple;

import ch.viascom.groundwork.restclient.exception.RESTClientException;
import ch.viascom.groundwork.restclient.http.request.PostRequest;
import ch.viascom.groundwork.restclient.response.generic.Response;
import lombok.Setter;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * @author patrick.boesch@viascom.ch
 */
public class SimplePostRequest<T extends Response> extends PostRequest<T> {

    @Setter
    private Class<T> parameterClass;

    public SimplePostRequest(String url, Class<T> parameterClass) throws RESTClientException {
        this(url, ContentType.APPLICATION_JSON.toString(), new StringEntity("", Consts.UTF_8), HttpClientBuilder.create().build(), parameterClass);
    }

    public SimplePostRequest(String url, HttpEntity requestBody, Class<T> parameterClass) throws RESTClientException {
        this(url, ContentType.APPLICATION_JSON.toString(), requestBody, HttpClientBuilder.create().build(), parameterClass);
    }

    public SimplePostRequest(String url, String mediaType, HttpEntity requestBody, Class<T> parameterClass) throws RESTClientException {
        this(url, mediaType, requestBody, HttpClientBuilder.create().build(), parameterClass);
    }

    public SimplePostRequest(String url, String mediaType, HttpEntity requestBody, HttpClient httpClient, Class<T> parameterClass) throws RESTClientException {
        this(url, "", mediaType, requestBody, httpClient, parameterClass);
    }

    public SimplePostRequest(String url, String path, String mediaType, HttpEntity requestBody, HttpClient httpClient, Class<T> parameterClass) throws RESTClientException {
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