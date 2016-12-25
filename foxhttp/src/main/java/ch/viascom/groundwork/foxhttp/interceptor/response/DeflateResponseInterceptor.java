package ch.viascom.groundwork.foxhttp.interceptor.response;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseInterceptorContext;
import lombok.Getter;

import java.io.InputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * @author patrick.boesch@viascom.ch
 */
public class DeflateResponseInterceptor implements FoxHttpResponseInterceptor {

    private boolean nowrap;
    @Getter
    private int weight;

    /**
     * @param nowrap if true then support GZIP compatible compression
     */
    public DeflateResponseInterceptor(boolean nowrap) {
        this(nowrap, 1);
    }

    /**
     * @param nowrap if true then support GZIP compatible compression
     */
    public DeflateResponseInterceptor(boolean nowrap, int weight) {
        super();
        this.nowrap = nowrap;
        this.weight = weight;
    }

    public DeflateResponseInterceptor(int weight) {
        this(false, weight);
    }

    public DeflateResponseInterceptor() {
        this(false, 1);
    }

    @Override
    public void onIntercept(FoxHttpResponseInterceptorContext context) throws FoxHttpException {
        try {
            if (context.getFoxHttpResponse().getResponseHeaders().getHeader("Content-Encoding") != null &&
                    "deflate".equals(context.getFoxHttpResponse().getResponseHeaders().getHeader("Content-Encoding").getValue())) {
                InputStream is = new InflaterInputStream(context.getFoxHttpResponse().getInputStreamBody(), new Inflater(nowrap));
                context.getFoxHttpResponse().getResponseBody().setBody(is, true);
            }
        } catch (Exception e) {
            throw new FoxHttpException(e);
        }
    }
}
