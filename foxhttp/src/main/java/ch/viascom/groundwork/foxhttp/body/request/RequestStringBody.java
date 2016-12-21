package ch.viascom.groundwork.foxhttp.body.request;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.type.ContentType;
import lombok.ToString;

/**
 * RequestStringBody for FoxHttp
 * <p>
 * Stores a string for a request body.
 *
 * @author patrick.boesch@viascom.ch
 */
@ToString
public class RequestStringBody extends FoxHttpRequestBody {
    private String content;

    /**
     * Create a new RequestStringBody
     *
     * @param content string content
     */
    public RequestStringBody(String content) {
        this.content = content;
        this.outputContentType = ContentType.DEFAULT_TEXT;
    }

    /**
     * Create a new RequestStringBody
     *
     * @param content     string content
     * @param contentType type of the content
     */
    public RequestStringBody(String content, ContentType contentType) {
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
        writeBody(context, content);
    }

    /**
     * Checks if the body contains data
     *
     * @return true if data is stored in the body
     */
    @Override
    public boolean hasBody() {
        return content != null && !content.isEmpty();
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
