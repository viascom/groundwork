package ch.viascom.groundwork.restclient.response.generic;

import lombok.Data;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class ErrorResponse implements Response {
    private ResponseHeader responseHeader;
    private String errorMessage;
    private String requestBody;
    private String responseBody;
}
