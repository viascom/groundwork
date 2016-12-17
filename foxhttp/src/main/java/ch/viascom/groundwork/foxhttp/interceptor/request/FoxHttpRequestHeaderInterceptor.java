package ch.viascom.groundwork.foxhttp.interceptor.request;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestHeaderInterceptorContext;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpRequestHeaderInterceptor extends FoxHttpInterceptor {
    void onIntercept(FoxHttpRequestHeaderInterceptorContext context) throws FoxHttpException;
}
