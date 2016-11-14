package ch.viascom.groundwork.restclient.response;

import ch.viascom.groundwork.restclient.response.generic.Response;
import ch.viascom.groundwork.restclient.response.generic.ResponseHeader;
import lombok.Data;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class NoContentResponse implements Response {
    private ResponseHeader responseHeader;
}
