package ch.viascom.groundwork.foxhttp.body.request;

import ch.viascom.groundwork.foxhttp.exception.FoxHttpRequestException;
import ch.viascom.groundwork.foxhttp.type.ContentType;
import ch.viascom.groundwork.serviceresult.ServiceResult;
import ch.viascom.groundwork.serviceresult.ServiceResultStatus;
import ch.viascom.groundwork.serviceresult.util.Metadata;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author patrick.boesch@viascom.ch
 */
@ToString
public class RequestServiceResultBody extends FoxHttpRequestBody {

    ServiceResult<? extends Serializable> serviceResult;

    public RequestServiceResultBody(ServiceResult<? extends Serializable> serviceResult) {
        this.serviceResult = serviceResult;
    }

    public RequestServiceResultBody(Serializable content) {
        this(ServiceResultStatus.successful, content);
    }

    public RequestServiceResultBody(ServiceResultStatus status, Serializable content) {
        this(status, content, content.getClass().getCanonicalName(), "");
    }

    public RequestServiceResultBody(ServiceResultStatus status, Serializable content, String type, String destination) {
        serviceResult = new ServiceResult<>(type, content, status);
        setDestination(destination);
    }

    @Override
    public void setBody(FoxHttpRequestBodyContext context) throws FoxHttpRequestException {
        if (context.getClient().getFoxHttpRequestParser() == null) {
            throw new FoxHttpRequestException("RequestServiceResultBody needs a FoxHttpRequestParser to serialize the body");
        }

        String json = context.getClient().getFoxHttpRequestParser().objectToSerialized(serviceResult);

        writeBody(context, json);
    }

    @Override
    public boolean hasBody() {
        return serviceResult != null;
    }

    @Override
    public ContentType getOutputContentType() {
        return ContentType.APPLICATION_JSON;
    }

    public void setStatus(ServiceResultStatus status) {
        this.serviceResult.setStatus(status);
    }

    public void setType(String type) {
        this.serviceResult.setType(type);
    }

    public void setHash(String hash) {
        this.serviceResult.setHash(hash);
    }

    public void setDestination(String destination) {
        this.serviceResult.setDestination(destination);
    }

    public void setMetadata(HashMap<String, Metadata> metadata) {
        this.serviceResult.setMetadata(metadata);
    }

    public void addMetadata(String key, Metadata<? extends Serializable> metadata) {
        this.serviceResult.getMetadata().put(key, metadata);
    }
}
