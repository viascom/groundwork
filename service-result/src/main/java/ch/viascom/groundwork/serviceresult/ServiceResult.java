package ch.viascom.groundwork.serviceresult;

import ch.viascom.groundwork.serviceresult.util.ObjectHasher;
import lombok.Data;

import java.io.Serializable;

/**
 * Represents a general response for all requests.
 *
 *  - hash: hash of the content
 *  - destination: this field can contain information about the response destination
 *
 * @param <T> Type of the content
 */
@Data
public class ServiceResult<T extends Serializable> {
	private ServiceResultStatus status;
	private T content;
	private String hash;
	private String destination;

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
}
