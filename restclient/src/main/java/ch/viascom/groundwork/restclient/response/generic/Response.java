package ch.viascom.groundwork.restclient.response.generic;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface Response {
    public ResponseHeader getResponseHeader();
    public void setResponseHeader(ResponseHeader responseHeader);
}
