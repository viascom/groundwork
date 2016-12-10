package ch.viascom.groundwork.foxhttp.body.response;

import ch.viascom.groundwork.foxhttp.type.RequestType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.net.URL;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
@AllArgsConstructor
public class FoxHttpResponseInformation {
    @Setter(AccessLevel.PROTECTED)
    private URL requestUrl;
    @Setter(AccessLevel.PROTECTED)
    private RequestType requestType;
}
