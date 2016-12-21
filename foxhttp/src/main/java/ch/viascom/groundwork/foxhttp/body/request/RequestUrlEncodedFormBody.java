package ch.viascom.groundwork.foxhttp.body.request;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.type.ContentType;
import ch.viascom.groundwork.foxhttp.type.HeaderTypes;
import ch.viascom.groundwork.foxhttp.util.QueryBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * RequestUrlEncodedFormBody for FoxHttp
 * <p>
 * Stores a fromData map for a request body.
 *
 * @author patrick.boesch@viascom.ch
 */
@ToString
public class RequestUrlEncodedFormBody extends FoxHttpRequestBody {
    @Getter
    @Setter
    private Map<String, String> formData = new HashMap<>();

    /**
     * Create a new RequestUrlEncodedFormBody
     */
    public RequestUrlEncodedFormBody() {
        this.outputContentType = ContentType.APPLICATION_FORM_URLENCODED;
    }

    /**
     * Create a new RequestUrlEncodedFormBody
     *
     * @param formData map of form data entries
     */
    public RequestUrlEncodedFormBody(Map<String, String> formData) {
        this.formData = formData;
        this.outputContentType = ContentType.APPLICATION_FORM_URLENCODED;
    }

    /**
     * Add a new form data entry
     *
     * @param key   key of the entry
     * @param value value of the entry
     */
    public void addFormEntry(String key, String value) {
        formData.put(key, value);
    }

    /**
     * Set the body of the request
     *
     * @param context context of the request
     * @throws FoxHttpRequestException can throw different exception based on input streams and interceptors
     */
    @Override
    public void setBody(FoxHttpRequestBodyContext context) throws FoxHttpRequestException {
        String formOutputData = QueryBuilder.buildQuery(formData);

        try {
            DataOutputStream wr = new DataOutputStream(outputStream);
            wr.writeBytes(formOutputData);
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

    /**
     * Checks if the body contains data
     *
     * @return true if data is stored in the body
     */
    @Override
    public boolean hasBody() {
        return formData.size() > 0;
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
