package ch.viascom.groundwork.foxhttp;

import ch.viascom.groundwork.foxhttp.authorization.DefaultAuthorizationStrategy;
import ch.viascom.groundwork.foxhttp.authorization.FoxHttpAuthorizationStrategy;
import ch.viascom.groundwork.foxhttp.cookie.DefaultCookieStore;
import ch.viascom.groundwork.foxhttp.cookie.FoxHttpCookieStore;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptor;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptorType;
import ch.viascom.groundwork.foxhttp.log.DefaultFoxHttpLogger;
import ch.viascom.groundwork.foxhttp.log.FoxHttpLogger;
import ch.viascom.groundwork.foxhttp.parser.FoxHttpParser;
import ch.viascom.groundwork.foxhttp.proxy.FoxHttpProxyStrategy;
import ch.viascom.groundwork.foxhttp.ssl.DefaultHostTrustStrategy;
import ch.viascom.groundwork.foxhttp.ssl.DefaultSSLTrustStrategy;
import ch.viascom.groundwork.foxhttp.ssl.FoxHttpHostTrustStrategy;
import ch.viascom.groundwork.foxhttp.ssl.FoxHttpSSLTrustStrategy;
import ch.viascom.groundwork.foxhttp.timeout.DefaultTimeoutStrategy;
import ch.viascom.groundwork.foxhttp.timeout.FoxHttpTimeoutStrategy;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author patrick.boesch@viascom.ch
 */
public class FoxHttpClient {

    @Getter
    @Setter
    //Response parser
    private FoxHttpParser foxHttpResponseParser;

    @Getter
    @Setter
    //Request parser
    private FoxHttpParser foxHttpRequestParser;

    @Getter
    @Setter
    //Interceptors
    private HashMap<FoxHttpInterceptorType, ArrayList<FoxHttpInterceptor>> foxHttpInterceptors = new HashMap<>();

    //@Getter
    //Caching
    //private FoxHttpCacheStrategy foxHttpCacheStrategy;

    @Getter
    @Setter
    //Cookies
    private FoxHttpCookieStore foxHttpCookieStore = new DefaultCookieStore();

    @Getter
    @Setter
    //Authorization
    private FoxHttpAuthorizationStrategy foxHttpAuthorizationStrategy = new DefaultAuthorizationStrategy();

    @Getter
    @Setter
    //Timeouts
    private FoxHttpTimeoutStrategy foxHttpTimeoutStrategy = new DefaultTimeoutStrategy();

    @Getter
    @Setter
    //HostnameVerifier
    private FoxHttpHostTrustStrategy foxHttpHostTrustStrategy = new DefaultHostTrustStrategy();

    @Getter
    @Setter
    //SSL
    private FoxHttpSSLTrustStrategy foxHttpSSLTrustStrategy = new DefaultSSLTrustStrategy();

    @Getter
    @Setter
    //Proxy
    private FoxHttpProxyStrategy foxHttpProxyStrategy;

    @Getter
    @Setter
    //Logger
    private FoxHttpLogger foxHttpLogger = new DefaultFoxHttpLogger(false);

    @Getter
    @Setter
    //UserAgent
    private String foxHttpUserAgent = "FoxHTTP v1.0";


    public void register(FoxHttpInterceptorType interceptorType, FoxHttpInterceptor foxHttpInterceptor) throws FoxHttpException {
        FoxHttpInterceptorType.verifyInterceptor(interceptorType, foxHttpInterceptor);
        if (foxHttpInterceptors.containsKey(interceptorType)) {
            foxHttpInterceptors.get(interceptorType).add(foxHttpInterceptor);
        } else {
            foxHttpInterceptors.put(interceptorType, new ArrayList<>(Arrays.asList(foxHttpInterceptor)));
        }
    }
}
