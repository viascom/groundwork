package ch.viascom.groundwork.foxhttp.component.oauth2;

import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationScope;
import ch.viascom.groundwork.foxhttp.type.RequestType;

import java.util.List;
import java.util.Map;

/**
 * OAuth2Store builder to create a new OAuth2Store
 *
 * @author patrick.boesch@viascom.ch
 */
public class OAuth2StoreBuilder {

    OAuth2Store oAuth2Store;

    /**
     * Create a new builder
     *
     * @param grantType Grant type used for token requests
     * @param authUrl Token request endpoint
     */
    public OAuth2StoreBuilder(GrantType grantType, String authUrl) {
        oAuth2Store = new OAuth2Store();
        oAuth2Store.setGrantType(grantType);
        oAuth2Store.setAuthUrl(authUrl);
    }

    /**
     * Set token request method
     *
     * @param authRequestType token request method
     * @return OAuth2StoreBuilder (this)
     */
    public OAuth2StoreBuilder setAuthRequestType(RequestType authRequestType) {
        oAuth2Store.setAuthRequestType(authRequestType);
        return this;
    }

    /**
     * Set requested scopes
     *
     * @param requestScopes Requested scopes
     * @return OAuth2StoreBuilder (this)
     */
    public OAuth2StoreBuilder setRequestScopes(String requestScopes) {
        oAuth2Store.setRequestScopes(requestScopes);
        return this;
    }

    /**
     * Set authorization scopes which needs a token
     *
     * @param authScopes authorization scopes which needs a token
     * @return OAuth2StoreBuilder (this)
     */
    public OAuth2StoreBuilder setFoxHttpAuthorizationScopes(List<FoxHttpAuthorizationScope> authScopes) {
        oAuth2Store.setAuthScopes(authScopes);
        return this;
    }

    /**
     * Add authorization scope which needs a token
     *
     * @param authScope authorization scope which needs a token
     * @return OAuth2StoreBuilder (this)
     */
    public OAuth2StoreBuilder addFoxHttpAuthorizationScope(FoxHttpAuthorizationScope authScope) {
        oAuth2Store.getAuthScopes().add(authScope);
        return this;
    }

    /**
     * Set additional parameters which will be included in the token request
     *
     * @param additionalParameters additional parameters which will be included in the token request
     * @return OAuth2StoreBuilder (this)
     */
    public OAuth2StoreBuilder setAdditionalParameters(Map<String, String> additionalParameters) {
        oAuth2Store.setAdditionalParameters(additionalParameters);
        return this;
    }

    /**
     * Add an additional parameter which will be included in the token request
     *
     * @param key additional parameter key
     * @param value additional parameter value
     * @return OAuth2StoreBuilder (this)
     */
    public OAuth2StoreBuilder addAdditionalParameter(String key, String value) {
        oAuth2Store.getAdditionalParameters().put(key, value);
        return this;
    }

    /**
     * Set username for password grant type
     *
     * @param username Username for password grant type
     * @return OAuth2StoreBuilder (this)
     */
    public OAuth2StoreBuilder setUsername(String username){
        oAuth2Store.setUsername(username);
        return this;
    }

    /**
     * Set password for password grant type
     *
     * @param password Password for password grant type
     * @return OAuth2StoreBuilder (this)
     */
    public OAuth2StoreBuilder setPassword(String password){
        oAuth2Store.setPassword(password);
        return this;
    }

    /**
     * Set client id for Client Credentials grant type or if client credentials are used for requests (useClientCredentials)
     *
     * @param clientId Client id for Client Credentials
     * @return OAuth2StoreBuilder (this)
     */
    public OAuth2StoreBuilder setClientId(String clientId){
        oAuth2Store.setClientId(clientId);
        return this;
    }

    /**
     * Set client secret for Client Credentials grant type or if client credentials are used for requests (useClientCredentials)
     *
     * @param clientSecret Client secret for Client Credentials
     * @return OAuth2StoreBuilder (this)
     */
    public OAuth2StoreBuilder setClientSecret(String clientSecret){
        oAuth2Store.setClientSecret(clientSecret);
        return this;
    }

    /**
     * Set true if Client Credentials should be used for all token requests
     *
     * IMPORTANT: If you set activateClientCredentialsUse to true you have to use the {@link #setClientId(String) setClientId}
     * and {@link #setClientSecret(String) setClientSecret} methods as well.
     *
     * @return OAuth2StoreBuilder (this)
     */
    public OAuth2StoreBuilder activateClientCredentialsUse(){
        oAuth2Store.setUseClientCredentials(true);
        return this;
    }

    /**
     * Set authorization Code for AuthorizationCode grant type
     * @param authorizationCode Authorization Code for AuthorizationCode grant type
     * @return OAuth2StoreBuilder (this)
     */
    public OAuth2StoreBuilder setAuthorizationCode(String authorizationCode){
        oAuth2Store.setAuthorizationCode(authorizationCode);
        return this;
    }

    /**
     * Get the OAuth2Store of this builder
     * @return OAuth2Store
     */
    public OAuth2Store build(){
        return oAuth2Store;
    }
}
