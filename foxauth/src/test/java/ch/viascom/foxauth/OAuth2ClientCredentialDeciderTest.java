package ch.viascom.foxauth;

import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author patrick.boesch@viascom.ch
 */
@Ignore
public class OAuth2ClientCredentialDeciderTest {

    @Test
    public void regexTest() {

        Pattern pattern = Pattern.compile("grant_type=([a-zA-Z_]*)&", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher("refresh_token=tGzv3JOkF0XG5Qx2TlKWIA&grant_type=client_credentials&client_id=s6BhdRkqt3&client_secret=7Fjfp0ZBr1KtDRbnfVdmIw");

        List<String> listMatches = new ArrayList<String>();

        while (matcher.find()) {
            listMatches.add(matcher.group(1));
        }

        String grantType = null;
        if (listMatches.size() > 0) {
            grantType = listMatches.get(0);
        }

        assertThat(grantType).isEqualTo("client_credentials");
    }

    @Test
    public void test() throws Exception {
        String authorization = "Authorization: Basic " + getBasicAuthenticationEncoding("FoxAuthorization","password1234");

        String[] authSplit = authorization.split(" ");
        String auth = authSplit[1];

        String[] usernamePassword = new String(Base64.getDecoder().decode(authSplit[2])).split(":");

        System.out.println(usernamePassword);
    }


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
