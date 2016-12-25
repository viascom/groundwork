package ch.viascom.groundwork.foxhttp.interceptors;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.response.FoxHttpResponseInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseInterceptorContext;

/**
 * @author patrick.boesch@viascom.ch
 */
public class ResponseInterceptor implements FoxHttpResponseInterceptor {
    @Override
    public void onIntercept(FoxHttpResponseInterceptorContext context) throws FoxHttpException {
        context.getFoxHttpResponse().setResponseCode(500);
    }

    @Override
    public int getWeight() {
        return 0;
    }
}
