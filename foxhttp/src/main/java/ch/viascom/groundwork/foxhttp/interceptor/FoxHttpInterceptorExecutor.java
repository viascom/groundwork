package ch.viascom.groundwork.foxhttp.interceptor;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.request.FoxHttpRequestBodyInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.FoxHttpRequestHeaderInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.FoxHttpRequestInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestBodyInterceptorContext;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestHeaderInterceptorContext;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestInterceptorContext;
import ch.viascom.groundwork.foxhttp.interceptor.response.FoxHttpResponseBodyInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.response.FoxHttpResponseCodeInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.response.FoxHttpResponseInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseBodyInterceptorContext;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseCodeInterceptorContext;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseInterceptorContext;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpInterceptorExecutor {

    public static void executeRequestInterceptor(FoxHttpRequestInterceptorContext context) throws FoxHttpException {
        if (context.getClient().getFoxHttpInterceptors().containsKey(FoxHttpInterceptorType.REQUEST)) {
            for (FoxHttpInterceptor interceptor : context.getClient().getFoxHttpInterceptors().get(FoxHttpInterceptorType.REQUEST)) {
                ((FoxHttpRequestInterceptor) interceptor).onIntercept(context);
            }
        }
    }

    public static void executeRequestHeaderInterceptor(FoxHttpRequestHeaderInterceptorContext context) throws FoxHttpException {
        if (context.getClient().getFoxHttpInterceptors().containsKey(FoxHttpInterceptorType.REQUEST_HEADER)) {
            for (FoxHttpInterceptor interceptor : context.getClient().getFoxHttpInterceptors().get(FoxHttpInterceptorType.REQUEST_HEADER)) {
                ((FoxHttpRequestHeaderInterceptor) interceptor).onIntercept(context);
            }
        }
    }

    public static void executeRequestBodyInterceptor(FoxHttpRequestBodyInterceptorContext context) throws FoxHttpException {
        if (context.getClient().getFoxHttpInterceptors().containsKey(FoxHttpInterceptorType.REQUEST_BODY)) {
            for (FoxHttpInterceptor interceptor : context.getClient().getFoxHttpInterceptors().get(FoxHttpInterceptorType.REQUEST_BODY)) {
                ((FoxHttpRequestBodyInterceptor) interceptor).onIntercept(context);
            }
        }
    }

    public static void executeResponseInterceptor(FoxHttpResponseInterceptorContext context) throws FoxHttpException {
        if (context.getClient().getFoxHttpInterceptors().containsKey(FoxHttpInterceptorType.RESPONSE)) {
            for (FoxHttpInterceptor interceptor : context.getClient().getFoxHttpInterceptors().get(FoxHttpInterceptorType.RESPONSE)) {
                ((FoxHttpResponseInterceptor) interceptor).onIntercept(context);
            }
        }
    }

    public static void executeResponseBodyInterceptor(FoxHttpResponseBodyInterceptorContext context) throws FoxHttpException {
        if (context.getClient().getFoxHttpInterceptors().containsKey(FoxHttpInterceptorType.RESPONSE_BODY)) {
            for (FoxHttpInterceptor interceptor : context.getClient().getFoxHttpInterceptors().get(FoxHttpInterceptorType.RESPONSE_BODY)) {
                ((FoxHttpResponseBodyInterceptor) interceptor).onIntercept(context);
            }
        }
    }

    public static void executeResponseCodeInterceptor(FoxHttpResponseCodeInterceptorContext context) throws FoxHttpException {
        if (context.getClient().getFoxHttpInterceptors().containsKey(FoxHttpInterceptorType.RESPONSE_CODE)) {
            for (FoxHttpInterceptor interceptor : context.getClient().getFoxHttpInterceptors().get(FoxHttpInterceptorType.RESPONSE_CODE)) {
                ((FoxHttpResponseCodeInterceptor) interceptor).onIntercept(context);
            }
        }
    }

}
