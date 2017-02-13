package ch.viascom.groundwork.foxhttp.interceptor.response;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseCodeInterceptorContext;

/**
 * @author patrick.boesch@viascom.ch
 */
public interface FoxHttpResponseCodeInterceptor extends FoxHttpInterceptor {
    void onIntercept(FoxHttpResponseCodeInterceptorContext context) throws FoxHttpException;
}
