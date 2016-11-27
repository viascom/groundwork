package ch.viascom.groundwork.security.encryption;

import lombok.Getter;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

@Getter
public class RSA {

	private PrivateKey privateKey;
	private PublicKey publicKey;

	public RSA setPrivateKey(BigInteger m, BigInteger e) throws NoSuchAlgorithmException, InvalidKeySpecException {
		RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
		KeyFactory fact = KeyFactory.getInstance("RSA");
		privateKey = fact.generatePrivate(keySpec);
		return this;
	}
	
	public RSA setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
		return this;
	}

	public RSA setPublicKey(BigInteger m, BigInteger e) throws NoSuchAlgorithmException, InvalidKeySpecException {
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
		KeyFactory fact = KeyFactory.getInstance("RSA");
		publicKey = fact.generatePublic(keySpec);
		return this;
	}

	public RSA setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
		return this;
	}

	public byte[] rsaEncrypt(byte[] data) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IOException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] cipherData = cipher.doFinal(data);
		return cipherData;
	}

	public byte[] rsaDecrypt(byte[] data) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			IOException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] cipherData = cipher.doFinal(data);
		return cipherData;
	}
}
