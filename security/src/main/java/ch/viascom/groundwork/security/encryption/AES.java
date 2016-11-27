package ch.viascom.groundwork.security.encryption;

import lombok.Getter;
import lombok.Setter;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Getter
@Setter
public class AES {

	private SecretKey secretKey;
	private SecretKeySpec secretKeySpec;
	IvParameterSpec ivspec;
	
	public static SecretKey generateKeyOnly() throws NoSuchAlgorithmException{
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		SecureRandom random = new SecureRandom();
		keyGen.init(random); 
		SecretKey secretKey = keyGen.generateKey();
		return secretKey;
	}
	
	public AES generateKey() throws NoSuchAlgorithmException{
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		SecureRandom random = new SecureRandom();
		keyGen.init(random); 
		secretKey = keyGen.generateKey();
		return this;
	}
	
	public AES generateSecretKeySpec(SecretKey secretKey){
		secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
		return this;
	}
	
	public AES generateSecretKeySpec() throws NoSuchAlgorithmException{
		generateKey();
		secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
		return this;
	}
	
	public AES setSecretKeySpecFromByteArray(byte[] secretKeyByteArray){
		secretKeySpec = new SecretKeySpec(secretKeyByteArray, 0, secretKeyByteArray.length, "AES");
		return this;
	}
	
	public AES generateIvParameterSpec(byte[] iv){
		ivspec = new IvParameterSpec(iv);
		return this;
	}
	
	public byte[] encrypt(byte[] input) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException{
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivspec);
		byte[] encrypted = cipher.doFinal(input);
		return encrypted;
	}
	
	public byte[] decrypt(byte[] input) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException{
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivspec);
		byte[] encrypted = cipher.doFinal(input);
		return encrypted;
	}
}
