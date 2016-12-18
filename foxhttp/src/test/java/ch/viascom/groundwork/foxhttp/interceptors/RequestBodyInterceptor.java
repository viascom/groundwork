package ch.viascom.groundwork.foxhttp.interceptors;

import ch.viascom.groundwork.foxhttp.interceptor.request.FoxHttpRequestBodyInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestBodyInterceptorContext;

import java.io.IOException;

/**
 * @author patrick.boesch@viascom.ch
 */
public class RequestBodyInterceptor implements FoxHttpRequestBodyInterceptor {
    @Override
    public void onIntercept(FoxHttpRequestBodyInterceptorContext context) {
        context.getRequestBody().getOutputStream().reset();
        try {
            context.getRequestBody().getOutputStream().write("New Body".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getWeight() {
        return 0;
    }
}
