package ch.viascom.groundwork.foxhttp.interceptor.response.context;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
import ch.viascom.groundwork.foxhttp.FoxHttpResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
@AllArgsConstructor
public class FoxHttpResponseBodyInterceptorContext {
    private int responseCode;
    private FoxHttpResponse foxHttpResponse;
    private FoxHttpRequest request;
    private FoxHttpClient client;
}
