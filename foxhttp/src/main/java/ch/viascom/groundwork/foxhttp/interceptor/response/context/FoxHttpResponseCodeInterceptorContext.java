package ch.viascom.groundwork.foxhttp.interceptor.response.context;

import ch.viascom.groundwork.foxhttp.FoxHttpClient;
import ch.viascom.groundwork.foxhttp.FoxHttpRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author patrick.boesch@viascom.ch
 */
@Data
@AllArgsConstructor
public class FoxHttpResponseCodeInterceptorContext {
    private int responseCode;
    private FoxHttpRequest request;
    private FoxHttpClient client;

}
