package ch.viascom.groundwork.foxhttp.interceptor.response;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.interceptor.response.context.FoxHttpResponseInterceptorContext;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

/**
 * @author patrick.boesch@viascom.ch
 */
public class GZipResponseInterceptor implements FoxHttpResponseInterceptor {
    @Override
    public void onIntercept(FoxHttpResponseInterceptorContext context) throws FoxHttpException {
        try {
            if ("gzip".equals(context.getFoxHttpResponse().getResponseHeaders().getHeader("Content-Encoding").getValue())) {
                InputStream is = new GZIPInputStream(context.getFoxHttpResponse().getInputStreamBody());
                context.getFoxHttpResponse().getResponseBody().setBody(new ByteArrayOutputStream());
                context.getFoxHttpResponse().getResponseBody().setBody(is);
            }
        } catch (Exception e) {
            throw new FoxHttpException(e);
        }
    }
}
