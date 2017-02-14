package ch.viascom.groundwork.foxhttp.component.oauth2.request;

import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
import ch.viascom.groundwork.foxhttp.component.oauth2.OAuth2Component;
import ch.viascom.groundwork.foxhttp.component.oauth2.OAuth2Store;
import ch.viascom.groundwork.foxhttp.builder.FoxHttpRequestBuilder;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;

import java.net.MalformedURLException;
import java.util.HashMap;

/**
 * @author patrick.boesch@viascom.ch
 */
public class DefaultOAuth2RefreshTokenRequestGenerator extends OAuth2RequestGenerator {
    @Override
    public FoxHttpRequest getRequest(OAuth2Component oAuth2Component) throws MalformedURLException, FoxHttpRequestException {
        OAuth2Store oAuth2Store = oAuth2Component.getOAuth2Store();
        FoxHttpRequestBuilder foxHttpRequestBuilder = new FoxHttpRequestBuilder(oAuth2Store.getAuthUrl(), oAuth2Store.getAuthRequestType(), oAuth2Component.getFoxHttpClient());

        HashMap<String,String> data = new HashMap<>();
        data.putAll(oAuth2Store.getAdditionalParameters());
        data.put("grant_type", "refresh_token");
        data.put("refresh_token", oAuth2Store.getRefreshToken());

        setData(oAuth2Store, foxHttpRequestBuilder, data);

        foxHttpRequestBuilder.setFollowRedirect(true);
        foxHttpRequestBuilder.setSkipResponseBody(false);

        return foxHttpRequestBuilder.build();
    }
}
