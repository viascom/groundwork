package ch.viascom.groundwork.foxhttp.body.request;

import ch.viascom.groundwork.foxhttp.body.FoxHttpRequestBodyContext;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.type.ContentType;
import ch.viascom.groundwork.foxhttp.type.HeaderTypes;

import java.io.DataOutputStream;
import java.io.Serializable;

/**
 * @author patrick.boesch@viascom.ch
 */
public class RequestObjectBody extends FoxHttpRequestBody {
    private Serializable content;


    public RequestObjectBody(Serializable content) {
        this.content = content;
        this.outputContentType = ContentType.APPLICATION_JSON;
    }

    public RequestObjectBody(Serializable content, ContentType contentType) {
        this.content = content;
        this.outputContentType = contentType;
    }

    @Override
    public void setBody(FoxHttpRequestBodyContext context) throws FoxHttpRequestException {
        if (context.getClient().getFoxHttpRequestParser() == null) {
            throw new FoxHttpRequestException("RequestObjectBody needs a FoxHttpRequestParser to serialize the body");
        }

        String json = context.getClient().getFoxHttpRequestParser().objectToJson(content);

        //Add Content-Length header if not exist
        if (context.getUrlConnection().getRequestProperty(HeaderTypes.CONTENT_LENGTH.toString()) == null) {
            context.getUrlConnection().setRequestProperty(HeaderTypes.CONTENT_LENGTH.toString(), Integer.toString(json.length()));
        }

        try {
            DataOutputStream wr = new DataOutputStream(outputStream);
            wr.writeBytes(json);
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
        return content != null;
    }

    @Override
    public ContentType getOutputContentType() {
        return outputContentType;
    }
}
