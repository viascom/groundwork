package ch.viascom.groundwork.foxhttp.interceptor.request;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestInterceptorContext;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpRequestInterceptor extends FoxHttpInterceptor {
    void onIntercept(FoxHttpRequestInterceptorContext context) throws FoxHttpException;
}
