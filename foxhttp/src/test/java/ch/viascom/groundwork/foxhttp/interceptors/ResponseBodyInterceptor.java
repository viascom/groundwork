package ch.viascom.groundwork.foxhttp.interceptors;

import ch.viascom.groundwork.foxhttp.body.response.FoxHttpResponseBody;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.response.FoxHttpResponseBodyInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseBodyInterceptorContext;

import java.io.IOException;

/**
 * @author patrick.boesch@viascom.ch
 */
public class ResponseBodyInterceptor implements FoxHttpResponseBodyInterceptor {
    @Override
    public void onIntercept(FoxHttpResponseBodyInterceptorContext context) throws FoxHttpException {
        context.getFoxHttpResponse().setResponseBody(new FoxHttpResponseBody());
        try {
            context.getFoxHttpResponse().getResponseBody().getBody().write("Hi!".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getWeight() {
        return 0;
    }
}
