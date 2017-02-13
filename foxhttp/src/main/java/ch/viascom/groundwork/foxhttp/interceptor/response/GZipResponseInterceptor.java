package ch.viascom.groundwork.foxhttp.interceptor.response;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseInterceptorContext;
import lombok.Getter;

import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * GZipResponseInterceptor automatically
 *
 * @author patrick.boesch@viascom.ch
 */
public class GZipResponseInterceptor implements FoxHttpResponseInterceptor {

    @Getter
    private int weight;

    public GZipResponseInterceptor() {
        this(1);
    }

    public GZipResponseInterceptor(int weight) {
        super();
        this.weight = weight;
    }

    @Override
    public void onIntercept(FoxHttpResponseInterceptorContext context) throws FoxHttpException {
        try {
            if (context.getFoxHttpResponse().getResponseHeaders().getHeader("Content-Encoding") != null &&
                    "gzip".equals(context.getFoxHttpResponse().getResponseHeaders().getHeader("Content-Encoding").getValue())) {
                InputStream is = new GZIPInputStream(context.getFoxHttpResponse().getInputStreamBody());
                context.getFoxHttpResponse().getResponseBody().setBody(is, true);
            }
        } catch (Exception e) {
            throw new FoxHttpException(e);
        }
    }
}
