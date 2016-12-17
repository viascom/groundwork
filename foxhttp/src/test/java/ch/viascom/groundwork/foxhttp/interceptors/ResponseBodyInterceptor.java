package ch.viascom.groundwork.foxhttp.interceptors;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.response.FoxHttpResponseBodyInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseBodyInterceptorContext;

/**
 * @author patrick.boesch@viascom.ch
 */
public class ResponseBodyInterceptor implements FoxHttpResponseBodyInterceptor {
    @Override
    public void onIntercept(FoxHttpResponseBodyInterceptorContext context) throws FoxHttpException {

    }

    @Override
    public int getWeight() {
        return 0;
    }
}
