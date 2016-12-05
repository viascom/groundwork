package ch.viascom.groundwork.restclient.response;

import ch.viascom.groundwork.restclient.response.generic.Response;
import ch.viascom.groundwork.restclient.response.generic.ResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JSONResponse implements Response {
    private ResponseHeader responseHeader;
    private String json;

    public JSONResponse(String json) {
        this.json = json;
    }
}
