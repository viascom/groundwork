package ch.viascom.groundwork.foxhttp.interceptor.request;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestConnectionInterceptorContext;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpRequestConnectionInterceptor extends FoxHttpInterceptor {
    void onIntercept(FoxHttpRequestConnectionInterceptorContext context) throws FoxHttpException;
}
