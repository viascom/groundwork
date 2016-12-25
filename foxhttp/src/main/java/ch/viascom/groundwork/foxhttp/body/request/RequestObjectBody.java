package ch.viascom.groundwork.foxhttp.body.request;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.type.ContentType;
import lombok.ToString;

import java.io.Serializable;

/**
 * RequestObjectBody for FoxHttp
 *
 * Stores an object for a request body.
 * To use this you have to set a RequestParser.
 *
 * @author patrick.boesch@viascom.ch
 */
@ToString
public class RequestObjectBody extends FoxHttpRequestBody {
    private Serializable content;

    /**
     * Create a new RequestObjectBody
     *
     * @param content serializable object
     */
    public RequestObjectBody(Serializable content) {
        this.content = content;
        this.outputContentType = ContentType.APPLICATION_JSON;
    }

    /**
     * Create a new RequestObjectBody
     *
     * @param content serializable object
     * @param contentType type of the content
     */
    public RequestObjectBody(Serializable content, ContentType contentType) {
        this.content = content;
        this.outputContentType = contentType;
    }

    /**
     * Set the body of the request
     *
     * @param context context of the request
     * @throws FoxHttpRequestException can throw different exception based on input streams and interceptors
     */
    @Override
    public void setBody(FoxHttpRequestBodyContext context) throws FoxHttpRequestException {
        if (context.getClient().getFoxHttpRequestParser() == null) {
            throw new FoxHttpRequestException("RequestObjectBody needs a FoxHttpRequestParser to serialize the body");
        }

        String json = context.getClient().getFoxHttpRequestParser().objectToSerialized(content);

        writeBody(context, json);
    }

    /**
     * Checks if the body contains data
     *
     * @return true if data is stored in the body
     */
    @Override
    public boolean hasBody() {
        return content != null;
    }

    /**
     * Get the ContentType of this body
     *
     * @return ContentType of this body
     */
    @Override
    public ContentType getOutputContentType() {
        return outputContentType;
    }
}
