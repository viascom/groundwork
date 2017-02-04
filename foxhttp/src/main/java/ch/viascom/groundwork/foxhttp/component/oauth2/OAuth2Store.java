package ch.viascom.groundwork.foxhttp.component.oauth2;

import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationScope;
import ch.viascom.groundwork.foxhttp.type.RequestType;
import lombok.Data;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class OAuth2Store {

    //Request
    private GrantType grantType;
    private String authUrl;
    private RequestType authRequestType = RequestType.POST;
    private String requestScopes = "";
    private FoxHttpAuthorizationScope authScope;
    private Map<String, String> additionalParameters = new HashMap<>();
    private boolean useClientCredentials = false;

    //Password
    private String username;
    private String password;

    //ClientCredentials
    private String clientId;
    private String clientSecret;

    //AuthorizationCode
    private String authorizationCode;

    //Response
    private String refreshToken;
    private String accessToken = "";
    private Long expirationTimeSeconds;
    private DateTime accessTokenTime;
    private DateTime accessTokenExpirationTime;
    private String scopes;
}
