package ch.viascom.groundwork.foxhttp.authorization;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.type.HeaderTypes;
import ch.viascom.groundwork.foxhttp.util.BasicAuthUtil;
import lombok.*;

import java.net.URLConnection;

/**
 * Basic Auth for FoxHttp
 * <p>
 * This FoxHttpAuthorization will create a header with data for a basic authentication.
 *
 * @author patrick.boesch@viascom.ch
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BasicAuthAuthorization implements FoxHttpAuthorization {

    private String username;
    private String password;

    @Override
    public void doAuthorization(URLConnection connection, FoxHttpAuthorizationScope foxHttpAuthorizationScope) throws FoxHttpRequestException {
        connection.setRequestProperty(HeaderTypes.AUTHORIZATION.toString(), "Basic " + BasicAuthUtil.getBasicAuthenticationEncoding(username, password));
    }


}
