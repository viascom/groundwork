package ch.viascom.groundwork.foxhttp.interceptor.response;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseInterceptorContext;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpResponseInterceptor extends FoxHttpInterceptor {
    void onIntercept(FoxHttpResponseInterceptorContext context) throws FoxHttpException;
}
