package ch.viascom.groundwork.foxhttp.interceptors;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.interceptor.response.FoxHttpResponseCodeInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseCodeInterceptorContext;

/**
 * @author patrick.boesch@viascom.ch
 */
public class ResponseCodeInterceptor implements FoxHttpResponseCodeInterceptor {
    @Override
    public void onIntercept(FoxHttpResponseCodeInterceptorContext context) throws FoxHttpRequestException {
    }
}
