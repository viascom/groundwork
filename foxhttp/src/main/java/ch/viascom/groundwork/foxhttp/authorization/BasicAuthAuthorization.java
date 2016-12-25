package ch.viascom.groundwork.foxhttp.authorization;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.type.HeaderTypes;
import lombok.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLConnection;

/**
 * Basic Auth for FoxHttp
 * <p>
 * This FoxHttpAuthorization will create a header with data for a basic authentication.
 *
 * @author patrick.boesch@viascom.ch
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BasicAuthAuthorization implements FoxHttpAuthorization {

    private String username;
    private String password;

    @Override
    public void doAuthorization(URLConnection connection, FoxHttpAuthorizationScope foxHttpAuthorizationScope) throws FoxHttpRequestException {
        connection.setRequestProperty(HeaderTypes.AUTHORIZATION.toString(), "Basic " + getBasicAuthenticationEncoding());
    }


    /**
     * Create user:password string for authentication.
     *
     * @return user:password string
     */
    private String getBasicAuthenticationEncoding() throws FoxHttpRequestException {
        String userPassword = username + ":" + password;

        Class<?> base64;
        try {
            base64 = Class.forName("java.util.Base64");
            Object objectToInvokeOn = base64.getEnclosingClass();
            Method encoderMethod = base64.getDeclaredMethod("getEncoder");
            Object encoder = encoderMethod.invoke(objectToInvokeOn);
            Method method = encoder.getClass().getDeclaredMethod("encodeToString", byte[].class);

            return (String) (method.invoke(encoder, (Object) userPassword.getBytes()));
        } catch (ClassNotFoundException e) {
            try {
                base64 = Class.forName("android.util.Base64");

                Object objectToInvokeOn = base64.getEnclosingClass();
                Method encoderMethod = base64.getDeclaredMethod("encodeToString", byte[].class, int.class);

                return (String) (encoderMethod.invoke(objectToInvokeOn, userPassword.getBytes(), 2));
            } catch (Exception e1) {
                throw new FoxHttpRequestException(e1);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new FoxHttpRequestException(e);
        }

    }
}
