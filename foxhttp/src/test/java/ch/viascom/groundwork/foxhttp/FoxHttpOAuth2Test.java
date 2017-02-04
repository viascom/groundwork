package ch.viascom.groundwork.foxhttp;

import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationScope;
import ch.viascom.groundwork.foxhttp.builder.FoxHttpClientBuilder;
import ch.viascom.groundwork.foxhttp.builder.FoxHttpRequestBuilder;
import ch.viascom.groundwork.foxhttp.component.oauth2.GrantType;
import ch.viascom.groundwork.foxhttp.component.oauth2.OAuth2Component;
import ch.viascom.groundwork.foxhttp.component.oauth2.OAuth2Store;
import ch.viascom.groundwork.foxhttp.component.oauth2.OAuth2StoreBuilder;
import ch.viascom.groundwork.foxhttp.log.SystemOutFoxHttpLogger;
import ch.viascom.groundwork.foxhttp.parser.GsonParser;
import ch.viascom.groundwork.foxhttp.type.RequestType;
import org.junit.Test;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpOAuth2Test {

    @Test
    public void test() throws Exception {
        FoxHttpClient httpClient = new FoxHttpClientBuilder(new GsonParser(), new GsonParser())
                .setFoxHttpLogger(new SystemOutFoxHttpLogger(true, "OAuth2"))
                .build();


        OAuth2StoreBuilder oAuth2StoreBuilder = new OAuth2StoreBuilder(GrantType.PASSWORD,"http://solara.viascom.ch/auth/token")
                .setAuthRequestType(RequestType.POST)
                .setFoxHttpAuthorizationScope(FoxHttpAuthorizationScope.create("http://solara.viascom.ch/account/*"))
                .setUsername("fox")
                .setPassword("password1234")
                .activateClientCredentialsUse()
                .setClientId("AluxApp/1.0-SNAPSHOT")
                .setClientSecret("7OVm8OeTPf6EZq3C");

        OAuth2Store oAuth2Store =oAuth2StoreBuilder.build();
        oAuth2Store.setAccessToken("5d6e38b7-a3b3-3206-9319-228cf2e7576f");

        OAuth2Component oAuth2Component = new OAuth2Component(oAuth2Store);
        httpClient.activateComponent(oAuth2Component);

        FoxHttpResponse response = new FoxHttpRequestBuilder("http://solara.viascom.ch/account/get", RequestType.GET, httpClient).build().execute();
        System.out.println(response.toString(true));

    }


}
