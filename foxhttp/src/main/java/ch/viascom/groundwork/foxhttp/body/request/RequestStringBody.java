package ch.viascom.groundwork.foxhttp.body.request;

import ch.viascom.groundwork.foxhttp.body.FoxHttpRequestBodyContext;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.type.ContentType;
import ch.viascom.groundwork.foxhttp.type.HeaderTypes;
import lombok.ToString;

import java.io.DataOutputStream;

/**
 * RequestStringBody for FoxHttp
 *
 * Stores a string for a request body.
 *
 * @author patrick.boesch@viascom.ch
 */
@ToString
public class RequestStringBody extends FoxHttpRequestBody {
    private String content;

    public RequestStringBody(String content) {
        this.content = content;
        this.outputContentType = ContentType.DEFAULT_TEXT;
    }

    public RequestStringBody(String content, ContentType contentType) {
        this.content = content;
        this.outputContentType = contentType;
    }

    @Override
    public void setBody(FoxHttpRequestBodyContext context) throws FoxHttpRequestException {
        //Add Content-Length header if not exist
        if (context.getUrlConnection().getRequestProperty(HeaderTypes.CONTENT_LENGTH.toString()) == null) {
            context.getUrlConnection().setRequestProperty(HeaderTypes.CONTENT_LENGTH.toString(), Integer.toString(content.length()));
        }

        try {
            DataOutputStream wr = new DataOutputStream(outputStream);
            wr.writeBytes(content);
            wr.flush();
            wr.close();

            //Execute interceptor
            executeInterceptor(context);

            context.getUrlConnection().getOutputStream().write(outputStream.toByteArray());
        } catch (Exception e) {
            throw new FoxHttpRequestException(e);
        }
    }

    @Override
    public boolean hasBody() {
        return (content != null && !content.isEmpty());
    }

    @Override
    public ContentType getOutputContentType() {
        return outputContentType;
    }
}
