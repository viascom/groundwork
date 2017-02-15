package ch.viascom.groundwork.foxhttp;

import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationScope;
import ch.viascom.groundwork.foxhttp.builder.FoxHttpClientBuilder;
import ch.viascom.groundwork.foxhttp.builder.FoxHttpRequestBuilder;
import ch.viascom.groundwork.foxhttp.component.oauth2.GrantType;
import ch.viascom.groundwork.foxhttp.component.oauth2.OAuth2Component;
import ch.viascom.groundwork.foxhttp.component.oauth2.OAuth2StoreBuilder;
import ch.viascom.groundwork.foxhttp.component.oauth2.response.OAuthTokenErrorResponse;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.log.SystemOutFoxHttpLogger;
import ch.viascom.groundwork.foxhttp.models.OAuth2Response;
import ch.viascom.groundwork.foxhttp.parser.GsonParser;
import ch.viascom.groundwork.foxhttp.response.serviceresult.FoxHttpServiceResultResponse;
import ch.viascom.groundwork.foxhttp.type.RequestType;
import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author patrick.boesch@viascom.ch
 */
@Ignore
public class FoxHttpOAuth2Test {

    // ==========================================================================
    // !! This test case is only testable if a OAuth2-Environment is available !!
    // !!  The following tests uses the non-public Viascom Solara Auth-Server  !!
    // !!     Viascom Solara Auth-Server does not implement plain OAuth 2      !!
    // ==========================================================================

    static FoxHttpClient httpClient;
    static OAuth2Component oAuth2Component;
    private static String solaraURL = "http://localhost:8080/solara-webservice-1.0-SNAPSHOT";
    private static String solaraTestUser = "fox@viascom.ch";
    private static String solaraTestPassword = "password1234";
    private static String solaraClient = "AluxApp/1.0-SNAPSHOT";
    private static String solaraClientSecret = "7OVm8OeTPf6EZq3C";

    @BeforeClass
    public static void prepareTestInstance() throws Exception {
        httpClient = new FoxHttpClientBuilder(new GsonParser(), new GsonParser())
                .setFoxHttpLogger(new SystemOutFoxHttpLogger(true, "OAuth2"))
                .addFoxHttpPlaceholderEntry("solara", solaraURL)
                .build();


        //Create Local-Demo-DB
        FoxHttpResponse demoCreateRequest = new FoxHttpRequestBuilder("{solara}/account/demo", RequestType.POST, httpClient).build().execute();
        System.out.println("-> DB created: " + demoCreateRequest.getResponseCode());


        OAuth2StoreBuilder oAuth2StoreBuilder = new OAuth2StoreBuilder(GrantType.PASSWORD, "{solara}/auth/token")
                .setAuthRequestType(RequestType.POST)
                .addFoxHttpAuthorizationScope(FoxHttpAuthorizationScope.create("{solara}/account"))
                .addFoxHttpAuthorizationScope(FoxHttpAuthorizationScope.create("{solara}/application"))
                .activateClientCredentialsUse()
                .setUsername(solaraTestUser)
                .setPassword(solaraTestPassword)
                .setClientId(solaraClient)
                .setClientSecret(solaraClientSecret)
                .setRequestScopes("solara-read");

        oAuth2Component = new OAuth2Component(oAuth2StoreBuilder.build());

        //Use Custom OAuth2RequestExecutor to parse ServiceResult
        oAuth2Component.setOAuth2RequestExecutor((foxHttpRequest, oAuth2Component1) -> {
            FoxHttpResponse response = foxHttpRequest.execute();
            if (response.getResponseCode() == 200) {
                FoxHttpServiceResultResponse serviceResultResponse = new FoxHttpServiceResultResponse(response);
                OAuth2Response tokenResponse = serviceResultResponse.getContent(OAuth2Response.class);

                oAuth2Component1.getOAuth2Store().setAccessToken(tokenResponse.getAccessToken());
                oAuth2Component1.getOAuth2Store().setRefreshToken(tokenResponse.getRefreshToken());
                oAuth2Component1.getOAuth2Store().setAccessTokenTime(new DateTime());
                oAuth2Component1.getOAuth2Store().setExpirationTimeSeconds(tokenResponse.getExpiresIn());
                oAuth2Component1.getOAuth2Store().setAccessTokenExpirationTime(
                        oAuth2Component1.getOAuth2Store().getAccessTokenTime().plusSeconds(oAuth2Component1.getOAuth2Store().getExpirationTimeSeconds().intValue())
                );

                for (FoxHttpAuthorizationScope scope : oAuth2Component1.getOAuth2Store().getAuthScopes()) {
                    oAuth2Component1.getFoxHttpClient().getFoxHttpAuthorizationStrategy().removeAuthorization(scope, oAuth2Component1.getOAuth2Authorization());
                    oAuth2Component1.getOAuth2Authorization().setValue(oAuth2Component1.getOAuth2Store().getAccessToken());
                    oAuth2Component1.getFoxHttpClient().getFoxHttpAuthorizationStrategy().addAuthorization(scope, oAuth2Component1.getOAuth2Authorization());
                }

            } else {
                OAuthTokenErrorResponse tokenErrorResponse = response.getParsedBody(OAuthTokenErrorResponse.class);
                throw new FoxHttpRequestException(tokenErrorResponse.getError() + " : " + tokenErrorResponse.getErrorDescription());
            }
        });
        httpClient.activateComponent(oAuth2Component);
    }

    @Test
    public void tokenFlowTest() throws Exception {
        FoxHttpResponse response = new FoxHttpRequestBuilder("{solara}/account", RequestType.GET, httpClient).build().execute();
        assertThat(response.getResponseCode()).isEqualTo(200);
        assertThat(oAuth2Component.getOAuth2Store().getAccessToken()).isNotEmpty();
        assertThat(oAuth2Component.getOAuth2Store().getRefreshToken()).isNotEmpty();


        String oldAccessToken = oAuth2Component.getOAuth2Store().getAccessToken();
        String oldRefreshToken = oAuth2Component.getOAuth2Store().getRefreshToken();

        //Set older expire-date
        oAuth2Component.getOAuth2Store().setAccessTokenExpirationTime(new DateTime().minusMinutes(1));

        response = new FoxHttpRequestBuilder("{solara}/account", RequestType.GET, httpClient).build().execute();
        assertThat(response.getResponseCode()).isEqualTo(200);
        assertThat(oAuth2Component.getOAuth2Store().getAccessToken()).isNotEmpty();
        assertThat(oAuth2Component.getOAuth2Store().getRefreshToken()).isNotEmpty();

        assertThat(oAuth2Component.getOAuth2Store().getAccessToken()).isNotEqualTo(oldAccessToken);
        assertThat(oAuth2Component.getOAuth2Store().getRefreshToken()).isNotEqualTo(oldRefreshToken);
    }


}
