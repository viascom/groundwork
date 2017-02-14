package ch.viascom.groundwork.foxhttp.interceptor;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.request.FoxHttpRequestBodyInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.FoxHttpRequestConnectionInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.FoxHttpRequestHeaderInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.FoxHttpRequestInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.response.FoxHttpResponseBodyInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.response.FoxHttpResponseCodeInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.response.FoxHttpResponseInterceptor;

/**
 * @author patrick.boesch@viascom.ch
 */
public enum FoxHttpInterceptorType {
    REQUEST(FoxHttpRequestInterceptor.class), REQUEST_CONNECTION(FoxHttpRequestConnectionInterceptor.class), REQUEST_HEADER(FoxHttpRequestHeaderInterceptor.class), REQUEST_BODY(FoxHttpRequestBodyInterceptor.class),
    RESPONSE(FoxHttpResponseInterceptor.class), RESPONSE_BODY(FoxHttpResponseBodyInterceptor.class), RESPONSE_CODE(FoxHttpResponseCodeInterceptor.class);

    private Class<? extends FoxHttpInterceptor> interceptorClass;

    FoxHttpInterceptorType(Class<? extends FoxHttpInterceptor> interceptorClass) {
        this.interceptorClass = interceptorClass;
    }

    public static void verifyInterceptor(FoxHttpInterceptorType interceptorType, FoxHttpInterceptor interceptor) throws FoxHttpException {
        Class<? extends FoxHttpInterceptor> interceptorTypeClass = interceptorType.getInterceptorClass();

        if (!interceptorTypeClass.isAssignableFrom(interceptor.getClass())) {
            throw new FoxHttpException("FoxHttpInterceptor " + interceptor.getClass() + " does not implement the right interface for the interceptor type " + interceptorType);
        }
    }

    public Class<? extends FoxHttpInterceptor> getInterceptorClass() {
        return this.interceptorClass;
    }
}
