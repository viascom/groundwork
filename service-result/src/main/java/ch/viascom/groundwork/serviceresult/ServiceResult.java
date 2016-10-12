package ch.viascom.groundwork.serviceresult;

import ch.viascom.groundwork.serviceresult.util.ObjectHasher;
import lombok.Data;

import java.io.Serializable;

@Data
public class ServiceResult<T extends Serializable> {
	private ServiceResultStatus status;
	private T content;
	private String hash;
	private String destination;
	private Object tag;
	
	public ServiceResult<T> setContent(T serviceResultContent) {
		content = serviceResultContent;
		setHash(ObjectHasher.hash(content));
		return this;
	}
}
