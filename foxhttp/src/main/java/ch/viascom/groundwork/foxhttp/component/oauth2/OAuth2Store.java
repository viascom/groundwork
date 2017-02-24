package ch.viascom.groundwork.foxhttp.component.oauth2;

import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationScope;
import ch.viascom.groundwork.foxhttp.type.RequestType;
import lombok.Data;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
public class OAuth2Store {

    //Request
    /**
     * Grant type used for token requests
     */
    private GrantType grantType;
    /**
     * Token request endpoint
     */
    private String authUrl;
    /**
     * Token request method
     */
    private RequestType authRequestType = RequestType.POST;
    /**
     * Requested scopes
     */
    private String requestScopes = "";
    /**
     * AuthorizationScope which needs a token
     */
    private List<FoxHttpAuthorizationScope> authScopes = new ArrayList<>();
    /**
     * Additional parameters which will be included in the token request
     */
    private Map<String, String> additionalParameters = new HashMap<>();
    /**
     * Should Client Credentials be used for all token requests?
     */
    private boolean useClientCredentials = false;

    //Password
    /**
     * Username for password grant type
     */
    private String username;
    /**
     * Password for password grant type
     */
    private String password;

    //ClientCredentials
    /**
     * Client id for Client Credentials grant type or if client credentials are used for requests (useClientCredentials)
     */
    private String clientId;
    /**
     * Client secret for Client Credentials grant type or if client credentials are used for requests (useClientCredentials)
     */
    private String clientSecret;

    //AuthorizationCode
    /**
     * Authorization Code for AuthorizationCode grant type
     */
    private String authorizationCode;

    //Response
    /**
     * Refresh token provided by the server
     */
    private String refreshToken;
    /**
     * Access token provided by the server
     */
    private String accessToken = "";
    /**
     * Expiration time in seconds provided by the server
     */
    private Long expirationTimeSeconds;
    /**
     * Create date time of the access token
     */
    private DateTime accessTokenTime;
    /**
     * Calculated expiration date time based on accessTokenTime and expirationTimeSeconds
     */
    private DateTime accessTokenExpirationTime;
    /**
     * Granted scopes by the server
     */
    private String scopes;
}
