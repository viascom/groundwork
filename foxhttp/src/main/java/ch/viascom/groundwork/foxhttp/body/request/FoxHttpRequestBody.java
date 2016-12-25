package ch.viascom.groundwork.foxhttp.body.request;

import ch.viascom.groundwork.foxhttp.body.FoxHttpBody;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpException;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.interceptor.FoxHttpInterceptorExecutor;
import ch.viascom.groundwork.foxhttp.interceptor.request.context.FoxHttpRequestBodyInterceptorContext;
import ch.viascom.groundwork.foxhttp.type.ContentType;
import ch.viascom.groundwork.foxhttp.type.HeaderTypes;
import lombok.Getter;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

/**
 * Abstract FoxHttpRequestBody
 * <p>
 * !! Do not use this class directly as body for a request. !!
 * <p>
 * Extend this class or use a default implementation:
 * - RequestMultipartBody
 * - RequestObjectBody
 * - RequestStringBody
 * - RequestUrlEncodedFormBody
 *
 * @author patrick.boesch@viascom.ch
 */
public abstract class FoxHttpRequestBody implements FoxHttpBody {
    @Getter
    protected ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ContentType outputContentType = ContentType.WILDCARD;

    public abstract void setBody(FoxHttpRequestBodyContext context) throws FoxHttpRequestException;

    public abstract boolean hasBody();

    public abstract ContentType getOutputContentType();

    protected void executeInterceptor(FoxHttpRequestBodyContext context) throws FoxHttpException {
        context.getRequest().getFoxHttpClient().getFoxHttpLogger().log("executeRequestBodyInterceptor()");
        FoxHttpInterceptorExecutor.executeRequestBodyInterceptor(
                new FoxHttpRequestBodyInterceptorContext(context.getUrlConnection(), this, context.getRequest(), context.getClient())
        );
    }

    public void writeBody(FoxHttpRequestBodyContext context, String json) throws FoxHttpRequestException {
        try {
            DataOutputStream wr = new DataOutputStream(outputStream);
            wr.writeBytes(json);
            wr.flush();
            wr.close();

            //Execute interceptor
            executeInterceptor(context);

            //Add Content-Length header if not exist
            if (context.getUrlConnection().getRequestProperty(HeaderTypes.CONTENT_LENGTH.toString()) == null) {
                context.getUrlConnection().setRequestProperty(HeaderTypes.CONTENT_LENGTH.toString(), Integer.toString(outputStream.size()));
            }

            context.getUrlConnection().getOutputStream().write(outputStream.toByteArray());
        } catch (Exception e) {
            throw new FoxHttpRequestException(e);
        }
    }
}
