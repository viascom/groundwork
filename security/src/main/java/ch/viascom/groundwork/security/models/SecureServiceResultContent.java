package ch.viascom.groundwork.security.models;

import java.io.Serializable;

public class SecureServiceResultContent implements Serializable{

	private String _secureContent;
	private String _secureKey;
	private String _secureIV;
	private String _secureContentClass;

	public String getSecureContent() {
		return _secureContent;
	}

	public SecureServiceResultContent setSecureContent(String secureContent) {
		_secureContent = secureContent;
		return this;
	}

	public String getSecureKey() {
		return _secureKey;
	}

	public SecureServiceResultContent setSecureKey(String secureKey) {
		_secureKey = secureKey;
		return this;
	}

	public String getSecureIV() {
		return _secureIV;
	}

	public SecureServiceResultContent setSecureIV(String secureIV) {
		_secureIV = secureIV;
		return this;
	}

	public String getSecureContentClass() {
		return _secureContentClass;
	}

	public SecureServiceResultContent setSecureContentClass(String secureContentClass) {
		_secureContentClass = secureContentClass;
		return this;
	}
}
