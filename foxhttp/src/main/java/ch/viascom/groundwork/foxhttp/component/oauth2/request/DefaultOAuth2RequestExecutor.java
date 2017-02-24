package ch.viascom.groundwork.foxhttp.component.oauth2.request;

import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
import ch.viascom.groundwork.foxhttp.FoxHttpResponse;
import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationScope;
import ch.viascom.groundwork.foxhttp.component.oauth2.OAuth2Component;
import ch.viascom.groundwork.foxhttp.component.oauth2.response.OAuthTokenErrorResponse;
import ch.viascom.groundwork.foxhttp.component.oauth2.response.OAuthTokenResponse;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import org.joda.time.DateTime;

/**
 * @author patrick.boesch@viascom.ch
 */
public class DefaultOAuth2RequestExecutor implements OAuth2RequestExecutor {
    public void executeOAuth2Request(FoxHttpRequest foxHttpRequest, OAuth2Component oAuth2Component) throws FoxHttpException {
        FoxHttpResponse response = foxHttpRequest.execute();
        if (response.getResponseCode() == 200) {
            OAuthTokenResponse tokenResponse = response.getParsedBody(OAuthTokenResponse.class);
            oAuth2Component.getOAuth2Store().setAccessToken(tokenResponse.getAccessToken());
            oAuth2Component.getOAuth2Store().setRefreshToken(tokenResponse.getRefreshToken());
            oAuth2Component.getOAuth2Store().setAccessTokenTime(new DateTime());
            oAuth2Component.getOAuth2Store().setExpirationTimeSeconds(tokenResponse.getExpiresInSeconds());
            oAuth2Component.getOAuth2Store().setAccessTokenExpirationTime(
                    oAuth2Component.getOAuth2Store().getAccessTokenTime().plusSeconds(oAuth2Component.getOAuth2Store().getExpirationTimeSeconds().intValue())
            );
            oAuth2Component.getOAuth2Store().setScopes(tokenResponse.getScope());

            for (FoxHttpAuthorizationScope scope : oAuth2Component.getOAuth2Store().getAuthScopes()) {
                oAuth2Component.getFoxHttpClient().getFoxHttpAuthorizationStrategy().removeAuthorization(scope, oAuth2Component.getOAuth2Authorization());
                oAuth2Component.getOAuth2Authorization().setValue(oAuth2Component.getOAuth2Store().getAccessToken());
                oAuth2Component.getFoxHttpClient().getFoxHttpAuthorizationStrategy().addAuthorization(scope, oAuth2Component.getOAuth2Authorization());
            }

        } else {
            OAuthTokenErrorResponse tokenErrorResponse = response.getParsedBody(OAuthTokenErrorResponse.class);
            throw new FoxHttpRequestException(tokenErrorResponse.getError() + " : " + tokenErrorResponse.getErrorDescription());
        }
    }
}
