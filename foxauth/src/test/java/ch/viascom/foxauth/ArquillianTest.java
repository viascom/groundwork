package ch.viascom.foxauth;

import ch.viascom.groundwork.foxauth.FoxAuth;
import ch.viascom.groundwork.foxhttp.FoxHttpResponse;
import ch.viascom.groundwork.foxhttp.builder.FoxHttpRequestBuilder;
import ch.viascom.groundwork.foxhttp.type.HeaderTypes;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.extension.rest.client.ArquillianResteasyResource;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.WebTarget;
import java.io.File;
import java.lang.reflect.Method;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author patrick.boesch@viascom.ch
 */
@RunWith(Arquillian.class)
public class ArquillianTest {

    @Deployment
    public static WebArchive createDeployment() {
        File[] files = Maven.resolver().loadPomFromFile("pom.xml")
                .importRuntimeDependencies().resolve().withTransitivity().asFile();
        WebArchive war = ShrinkWrap
                .create(WebArchive.class)
                .addClass(RESTService.class)
                .addPackages(true, FoxAuth.class.getPackage())
                .addAsLibraries(files)
                .setWebXML("web.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");

        return war;
    }

    @Test
    @RunAsClient
    public void clientCredentialsGetSuccess(@ArquillianResteasyResource final WebTarget webTarget) throws Exception {
        FoxHttpResponse response = new FoxHttpRequestBuilder<>(webTarget.getUri().toString() + "/get")
                .addRequestQueryEntry("grant_type", "client_credentials")
                .addRequestHeader(HeaderTypes.AUTHORIZATION, "Basic " + getBasicAuthenticationEncoding("FoxAuth", "password1234"))
                .build().execute();

        assertThat(response.getStringBody()).isEqualTo("OK !");
    }
/*
    @Test
    @RunAsClient
    public void clientCredentialsPostSuccess(@ArquillianResteasyResource final WebTarget webTarget) throws Exception {
        RequestUrlEncodedFormBody formBody = new RequestUrlEncodedFormBody();
        formBody.addFormEntry("grant_type", "client_credentials");

        FoxHttpResponse response = new FoxHttpRequestBuilder<>(webTarget.getUri().toString() + "/post", RequestType.POST)
                .setRequestBody(formBody)
                .addRequestHeader(HeaderTypes.AUTHORIZATION, "Basic " + getBasicAuthenticationEncoding("FoxAuth1", "password1234"))
                .build().execute();
        System.out.println(response.toString(true));
        assertThat(response.getStringBody()).isEqualTo("OK !");
    }
*/

    /**
     * Create user:password string for authentication.
     *
     * @return user:password string
     */
    public String getBasicAuthenticationEncoding(String username, String password) throws Exception {
        String userPassword = username + ":" + password;

        Class<?> base64;
        base64 = Class.forName("java.util.Base64");
        Object objectToInvokeOn = base64.getEnclosingClass();
        Method encoderMethod = base64.getDeclaredMethod("getEncoder");
        Object encoder = encoderMethod.invoke(objectToInvokeOn);
        Method method = encoder.getClass().getDeclaredMethod("encodeToString", byte[].class);

        return (String) (method.invoke(encoder, (Object) userPassword.getBytes()));
    }
}
