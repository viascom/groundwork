package ch.viascom.groundwork.foxhttp.interceptor.request;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestBodyInterceptorContext;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpRequestBodyInterceptor extends FoxHttpInterceptor {
    void onIntercept(FoxHttpRequestBodyInterceptorContext context) throws FoxHttpException;
}
