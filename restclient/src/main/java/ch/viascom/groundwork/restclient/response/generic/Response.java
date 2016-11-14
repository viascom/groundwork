package ch.viascom.groundwork.restclient.response.generic;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface Response {
    ResponseHeader getResponseHeader();
    void setResponseHeader(ResponseHeader responseHeader);
}
