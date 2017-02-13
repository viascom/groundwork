package ch.viascom.groundwork.foxhttp.response.serviceresult;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.response.FoxHttpResponseInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseInterceptorContext;
import ch.viascom.groundwork.serviceresult.exception.ServiceFault;

/**
 * @author patrick.boesch@viascom.ch
 */
public class DefaultServiceResultFaultInterceptor implements FoxHttpResponseInterceptor {

    /**
     * Set as last interceptors of all
     *
     * @return Integer.MAX_VALUE
     */
    @Override
    public int getWeight() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onIntercept(FoxHttpResponseInterceptorContext context) throws FoxHttpException {
        if (!isValidResponseCode(context.getResponseCode())) {
            ServiceFault serviceFault = new FoxHttpServiceResultResponse(context.getFoxHttpResponse()).getFault();
            throw new FoxHttpServiceResultException(serviceFault);
        }
    }

    /**
     * Check if the status code is valid
     *
     * @param statuscode status code of the response
     * @return true if the status code is valid.
     */
    public boolean isValidResponseCode(int statuscode) {
        return (statuscode >= 200 && statuscode < 300);
    }
}
