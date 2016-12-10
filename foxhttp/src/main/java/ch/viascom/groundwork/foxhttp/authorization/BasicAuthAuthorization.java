package ch.viascom.groundwork.foxhttp.authorization;

import ch.viascom.groundwork.foxhttp.type.HeaderTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URLConnection;
import java.util.Base64;

/**
 * @author patrick.boesch@viascom.ch
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BasicAuthAuthorization implements FoxHttpAuthorization {

    private String username;
    private String password;

    @Override
    public void doAuthorization(URLConnection connection, FoxHttpAuthorizationScope foxHttpAuthorizationScope) {
        connection.setRequestProperty(HeaderTypes.AUTHORIZATION.toString(), "Basic " + getBasicAuthenticationEncoding());
    }


    private String getBasicAuthenticationEncoding() {
        String userPassword = username + ":" + password;
        return new String(Base64.getEncoder().encode(userPassword.getBytes()));
    }
}
