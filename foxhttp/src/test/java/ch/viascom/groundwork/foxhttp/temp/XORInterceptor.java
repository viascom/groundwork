package ch.viascom.groundwork.foxhttp.temp;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.request.FoxHttpRequestBodyInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestBodyInterceptorContext;

import java.io.IOException;

/**
 * @author patrick.boesch@viascom.ch
 */
public class XORInterceptor implements FoxHttpRequestBodyInterceptor {

    private String key;

    public XORInterceptor(String key) {
        super();
        this.key = key;
    }

    public byte[] xor(final byte[] input, final byte[] secret) {
        final byte[] output = new byte[input.length];
        if (secret.length == 0) {
            throw new IllegalArgumentException("empty security key");
        }
        int spos = 0;
        for (int pos = 0; pos < input.length; ++pos) {
            output[pos] = (byte) (input[pos] ^ secret[spos]);
            ++spos;
            if (spos >= secret.length) {
                spos = 0;
            }
        }
        return output;
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public void onIntercept(FoxHttpRequestBodyInterceptorContext context) throws FoxHttpException {

        //Get current body and save it as byte[]
        byte[] body = xor(context.getRequestBody().getOutputStream().toByteArray(), key.getBytes());

        //Reset body
        context.getRequestBody().getOutputStream().reset();

        try {
            //Write ne encrypted body
            context.getRequestBody().getOutputStream().write(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
