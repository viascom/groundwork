package ch.viascom.groundwork.foxhttp.interceptors;

import ch.viascom.groundwork.foxhttp.interceptor.request.FoxHttpRequestHeaderInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestHeaderInterceptorContext;

/**
 * @author patrick.boesch@viascom.ch
 */
public class RequestHeaderInterceptor implements FoxHttpRequestHeaderInterceptor {
    @Override
    public void onIntercept(FoxHttpRequestHeaderInterceptorContext context) {
        context.getUrlConnection().addRequestProperty("Header-Key","Fox");
    }

    @Override
    public int getWeight() {
        return 0;
    }
}
