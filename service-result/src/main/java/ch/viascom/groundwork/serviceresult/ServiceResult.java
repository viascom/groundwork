package ch.viascom.groundwork.serviceresult;

import ch.viascom.groundwork.serviceresult.util.Metadata;
import ch.viascom.groundwork.serviceresult.util.ObjectHasher;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Represents a general response for all requests.
 * <p>
 * - hash: hash of the content
 * - destination: this field can contain information about the response destination
 *
 * @param <T> Type of the content
 */
@Data
public class ServiceResult<T extends Serializable> implements Serializable {
    private ServiceResultStatus status;
    private String type;
    private T content;
    private String hash;
    private String destination;
    private HashMap<String, Metadata> metadata = new HashMap<>();

    public ServiceResult(String type) {
        this.setType(type);
    }

    public ServiceResult(Class<T> type) {
        this.setType(type.getCanonicalName());
    }

    public ServiceResult(String type, T serviceResultContent) {
        this.setType(type);
        this.setContent(serviceResultContent);
    }

    public ServiceResult(Class<T> type, T serviceResultContent) {
        this.setType(type.getCanonicalName());
        this.setContent(serviceResultContent);
    }

    public ServiceResult(String type, T serviceResultContent, ServiceResultStatus status) {
        this.setType(type);
        this.setContent(serviceResultContent);
        this.setStatus(status);
    }

    public ServiceResult(Class<T> type, T serviceResultContent, ServiceResultStatus status) {
        this.setType(type.getCanonicalName());
        this.setContent(serviceResultContent);
        this.setStatus(status);
    }

    /**
     * Set the content of the ServiceResult
     *
     * @param serviceResultContent
     * @return
     */
    public ServiceResult<T> setContent(T serviceResultContent) {
        content = serviceResultContent;
        setHash(ObjectHasher.hash(content));
        return this;
    }

    /**
     * Add a new set of Metadata
     *
     * @param key
     * @param metadata
     * @return
     */
    public ServiceResult<T> addMetadata(String key, Metadata metadata) {
        this.getMetadata().put(key, metadata);
        return this;
    }
}
