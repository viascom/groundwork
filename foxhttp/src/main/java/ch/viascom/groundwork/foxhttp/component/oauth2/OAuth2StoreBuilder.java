package ch.viascom.groundwork.foxhttp.component.oauth2;

import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationScope;
import ch.viascom.groundwork.foxhttp.type.RequestType;

import java.util.Map;

/**
 * @author patrick.boesch@viascom.ch
 */
public class OAuth2StoreBuilder {

    OAuth2Store oAuth2Store;

    public OAuth2StoreBuilder(GrantType grantType, String authUrl) {
        oAuth2Store = new OAuth2Store();
        oAuth2Store.setGrantType(grantType);
        oAuth2Store.setAuthUrl(authUrl);
    }

    public OAuth2StoreBuilder setAuthRequestType(RequestType authRequestType) {
        oAuth2Store.setAuthRequestType(authRequestType);
        return this;
    }

    public OAuth2StoreBuilder setRequestScopes(String requestScopes) {
        oAuth2Store.setRequestScopes(requestScopes);
        return this;
    }

    public OAuth2StoreBuilder setFoxHttpAuthorizationScope(FoxHttpAuthorizationScope authScope) {
        oAuth2Store.setAuthScope(authScope);
        return this;
    }

    public OAuth2StoreBuilder setAdditionalParameters(Map<String, String> additionalParameters) {
        oAuth2Store.setAdditionalParameters(additionalParameters);
        return this;
    }

    public OAuth2StoreBuilder addAdditionalParameter(String key, String value) {
        oAuth2Store.getAdditionalParameters().put(key, value);
        return this;
    }

    public OAuth2StoreBuilder setUsername(String username){
        oAuth2Store.setUsername(username);
        return this;
    }

    public OAuth2StoreBuilder setPassword(String password){
        oAuth2Store.setPassword(password);
        return this;
    }

    public OAuth2StoreBuilder setClientId(String clientId){
        oAuth2Store.setClientId(clientId);
        return this;
    }

    public OAuth2StoreBuilder setClientSecret(String clientSecret){
        oAuth2Store.setClientSecret(clientSecret);
        return this;
    }

    public OAuth2StoreBuilder activateClientCredentialsUse(){
        oAuth2Store.setUseClientCredentials(true);
        return this;
    }

    public OAuth2StoreBuilder setAuthorizationCode(String authorizationCode){
        oAuth2Store.setAuthorizationCode(authorizationCode);
        return this;
    }

    public OAuth2Store build(){
        return oAuth2Store;
    }
}
