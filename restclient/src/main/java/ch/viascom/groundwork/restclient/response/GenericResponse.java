package ch.viascom.groundwork.restclient.response;

import ch.viascom.groundwork.restclient.response.generic.Response;
import ch.viascom.groundwork.restclient.response.generic.ResponseHeader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.HttpEntity;

/**
 * @author patrick.boesch@viascom.ch 20.04.16.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse implements Response {
    private ResponseHeader responseHeader;
    private HttpEntity entity;

    public GenericResponse(HttpEntity entity) {
        this.entity = entity;
    }
}
