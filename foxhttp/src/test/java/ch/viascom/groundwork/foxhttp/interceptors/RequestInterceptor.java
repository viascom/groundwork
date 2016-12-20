package ch.viascom.groundwork.foxhttp.interceptors;

import ch.viascom.groundwork.foxhttp.interceptor.request.FoxHttpRequestInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestInterceptorContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author patrick.boesch@viascom.ch
 */
@NoArgsConstructor
@AllArgsConstructor
public class RequestInterceptor implements FoxHttpRequestInterceptor {

    @Getter
    private int weight = 0;

    @Override
    public void onIntercept(FoxHttpRequestInterceptorContext context) {
        context.getRequest().getRequestQuery().addQueryEntry("key","Fox");
    }

}
