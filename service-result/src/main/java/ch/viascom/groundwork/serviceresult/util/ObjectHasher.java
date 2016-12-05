package ch.viascom.groundwork.serviceresult.util;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Provides an object hashing function
 */
public class ObjectHasher {

	/**
	 * Create a hash of a serializable object
     *
	 * @param obj Serializable object
	 * @return Hash of the serializable object
	 */
	public static String hash(Serializable obj) {

		if (obj == null) {
			return "";
		}
		StringBuilder hexString = new StringBuilder();
		try {

			MessageDigest m = MessageDigest.getInstance("SHA1");
			m.update(SerializationUtils.serialize(obj));

			byte[] mdbytes = m.digest();

            for (byte mdbyte : mdbytes) {
                hexString.append(Integer.toHexString(0xFF & mdbyte));
            }

		} catch (NoSuchAlgorithmException e) {
			return "";
		}
		return hexString.toString();
	}

}
