package ch.viascom.groundwork.foxhttp.response;

import ch.viascom.groundwork.foxhttp.FoxHttpResponse;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpResponseException;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpResponseParser {
    FoxHttpResponseParser parseResult(FoxHttpResponse foxHttpResponse) throws FoxHttpResponseException;

    String getStringBody() throws IOException;

    <T extends Serializable> T getContent(Class<T> contentClass) throws FoxHttpResponseException;

    <T extends Serializable> T getContent(Class<T> contentClass, boolean checkHash) throws FoxHttpResponseException;
}
