package ch.viascom.groundwork.restclient.request;

import ch.viascom.groundwork.restclient.response.generic.Response;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface PostRequestInterface<T extends Response> extends RequestInterface<T> {
    Object request() throws IOException, URISyntaxException;
}
