package ch.viascom.groundwork.foxhttp.interceptor.response;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseInterceptorContext;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * @author patrick.boesch@viascom.ch
 */
public class DeflateResponseInterceptor implements FoxHttpResponseInterceptor {

    private boolean nowrap;

    /**
     *
     * @param nowrap if true then support GZIP compatible compression
     */
    public DeflateResponseInterceptor(boolean nowrap) {
        super();
        this.nowrap = nowrap;
    }

    public DeflateResponseInterceptor() {
        super();
        this.nowrap = false;
    }

    @Override
    public void onIntercept(FoxHttpResponseInterceptorContext context) throws FoxHttpException {
        try {
            if ("deflate".equals(context.getFoxHttpResponse().getResponseHeaders().getHeader("Content-Encoding").getValue())) {
                InputStream is = new InflaterInputStream(context.getFoxHttpResponse().getInputStreamBody(), new Inflater(nowrap));
                context.getFoxHttpResponse().getResponseBody().setBody(new ByteArrayOutputStream());
                context.getFoxHttpResponse().getResponseBody().setBody(is);
            }
        } catch (Exception e) {
            throw new FoxHttpException(e);
        }
    }
}
