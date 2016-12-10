package ch.viascom.groundwork.foxhttp.authorization;

import ch.viascom.groundwork.foxhttp.type.HeaderTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URLConnection;

/**
 * @author patrick.boesch@viascom.ch
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BearerTokenAuthorization implements FoxHttpAuthorization {

    private String token;

    @Override
    public void doAuthorization(URLConnection connection, FoxHttpAuthorizationScope foxHttpAuthorizationScope) {
        connection.setRequestProperty(HeaderTypes.AUTHORIZATION.toString(), "Bearer " + token);
    }
}
