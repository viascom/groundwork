package ch.viascom.groundwork.foxhttp.authorization;

import ch.viascom.groundwork.foxhttp.type.HeaderTypes;
import lombok.*;

import java.net.URLConnection;
import java.util.Base64;

/**
 * Basic Auth for FoxHttp
 *
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
    public void doAuthorization(URLConnection connection, FoxHttpAuthorizationScope foxHttpAuthorizationScope) {
        connection.setRequestProperty(HeaderTypes.AUTHORIZATION.toString(), "Basic " + getBasicAuthenticationEncoding());
    }


    /**
     * Create user:password string for authentication.
     *
     * @return user:password string
     */
    private String getBasicAuthenticationEncoding() {
        String userPassword = username + ":" + password;
        return new String(Base64.getEncoder().encode(userPassword.getBytes()));
    }
}
