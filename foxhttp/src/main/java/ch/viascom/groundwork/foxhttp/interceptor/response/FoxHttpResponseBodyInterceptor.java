package ch.viascom.groundwork.foxhttp.interceptor.response;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseBodyInterceptorContext;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpResponseBodyInterceptor extends FoxHttpInterceptor {
    void onIntercept(FoxHttpResponseBodyInterceptorContext context) throws FoxHttpException;
}
