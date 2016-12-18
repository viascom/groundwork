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
        if(context.getResponseCode() == 404){
            throw new FoxHttpRequestException("Found 404");
        }
    }

    @Override
    public int getWeight() {
        return 0;
    }
}
