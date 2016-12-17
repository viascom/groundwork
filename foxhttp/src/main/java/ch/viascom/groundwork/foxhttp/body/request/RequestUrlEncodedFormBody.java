package ch.viascom.groundwork.foxhttp.body.request;

import ch.viascom.groundwork.foxhttp.body.FoxHttpRequestBodyContext;
import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.type.ContentType;
import ch.viascom.groundwork.foxhttp.type.HeaderTypes;
import ch.viascom.groundwork.foxhttp.util.QueryBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.DataOutputStream;
import java.util.HashMap;

/**
 * RequestUrlEncodedFormBody for FoxHttp
 *
 * Stores a fromData map for a request body.
 *
 * @author patrick.boesch@viascom.ch
 */
@ToString
public class RequestUrlEncodedFormBody extends FoxHttpRequestBody {
    @Getter
    @Setter
    private HashMap<String, String> formData = new HashMap<>();

    public RequestUrlEncodedFormBody() {
        this.outputContentType = ContentType.APPLICATION_FORM_URLENCODED;
    }

    public RequestUrlEncodedFormBody(HashMap<String, String> formData) {
        this.formData = formData;
        this.outputContentType = ContentType.APPLICATION_FORM_URLENCODED;
    }

    public void addFormEntry(String key, String value) {
        formData.put(key, value);
    }

    @Override
    public void setBody(FoxHttpRequestBodyContext context) throws FoxHttpRequestException {
        String formOutputData = QueryBuilder.buildQuery(formData);

        //Add Content-Length header if not exist
        if (context.getUrlConnection().getRequestProperty(HeaderTypes.CONTENT_LENGTH.toString()) == null) {
            context.getUrlConnection().setRequestProperty(HeaderTypes.CONTENT_LENGTH.toString(), Integer.toString(formOutputData.length()));
        }

        try {
            DataOutputStream wr = new DataOutputStream(outputStream);
            wr.writeBytes(formOutputData);
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
        return formData.size() > 0;
    }

    @Override
    public ContentType getOutputContentType() {
        return outputContentType;
    }

}
