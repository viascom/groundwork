package ch.viascom.groundwork.foxhttp.interceptor.request;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestHeaderInterceptorContext;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpRequestHeaderInterceptor {
    void onIntercept(FoxHttpRequestHeaderInterceptorContext context) throws FoxHttpException;
}
