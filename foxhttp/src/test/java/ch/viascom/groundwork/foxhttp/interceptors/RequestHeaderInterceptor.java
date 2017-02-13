package ch.viascom.groundwork.foxhttp.interceptors;

import ch.viascom.groundwork.foxhttp.interceptor.request.FoxHttpRequestHeaderInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestHeaderInterceptorContext;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author patrick.boesch@viascom.ch
 */
@NoArgsConstructor
@AllArgsConstructor
public class RequestHeaderInterceptor implements FoxHttpRequestHeaderInterceptor {

    @Getter
    private int weight = 0;

    @Override
    public void onIntercept(FoxHttpRequestHeaderInterceptorContext context) {
        context.getUrlConnection().addRequestProperty("Header-Key", "Fox");
    }

}
