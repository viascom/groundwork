package ch.viascom.groundwork.foxhttp;

import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationScope;
import ch.viascom.groundwork.foxhttp.builder.FoxHttpClientBuilder;
import ch.viascom.groundwork.foxhttp.builder.FoxHttpRequestBuilder;
import ch.viascom.groundwork.foxhttp.component.oauth2.GrantType;
import ch.viascom.groundwork.foxhttp.component.oauth2.OAuth2Component;
import ch.viascom.groundwork.foxhttp.component.oauth2.OAuth2StoreBuilder;
import ch.viascom.groundwork.foxhttp.log.SystemOutFoxHttpLogger;
import ch.viascom.groundwork.foxhttp.parser.GsonParser;
import ch.viascom.groundwork.foxhttp.type.RequestType;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author patrick.boesch@viascom.ch
 */
@Ignore
public class FoxHttpOAuth2Test {

    // ==========================================================================
    // !! This test case is only testable if a OAuth2-Environment is available !!
    // !!  The following tests uses the non-public Viascom Solara Auth-Server  !!
    // ==========================================================================

    @Test
    public void test() throws Exception {
        FoxHttpClient httpClient = new FoxHttpClientBuilder(new GsonParser(), new GsonParser())
                .setFoxHttpLogger(new SystemOutFoxHttpLogger(true, "OAuth2"))
                .build();


        OAuth2StoreBuilder oAuth2StoreBuilder = new OAuth2StoreBuilder(GrantType.PASSWORD,"http://localhost:8080/solara-1.0-SNAPSHOT/auth/token")
                .setAuthRequestType(RequestType.POST)
                .setFoxHttpAuthorizationScope(FoxHttpAuthorizationScope.create("http://localhost:8080/solara-1.0-SNAPSHOT/account/*"))
                .activateClientCredentialsUse()
                .setUsername("fox")
                .setPassword("password1234")
                .setClientId("AluxApp/1.0-SNAPSHOT")
                .setClientSecret("7OVm8OeTPf6EZq3C")
                .setRequestScopes("solara-read");

        OAuth2Component oAuth2Component = new OAuth2Component(oAuth2StoreBuilder.build());
        httpClient.activateComponent(oAuth2Component);

        FoxHttpResponse response = new FoxHttpRequestBuilder("http://localhost:8080/solara-1.0-SNAPSHOT/account/get", RequestType.GET, httpClient).build().execute();
        System.out.println(response.toString(true));

    }


}
