package ch.viascom.groundwork.security.encryption;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class RSAKeyLoader {

	public static PrivateKey getPrivateKeyFromFile(InputStream keyFile) throws IOException, NoSuchAlgorithmException,
			InvalidKeySpecException, ClassNotFoundException {
		ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(keyFile));
		BigInteger m = (BigInteger) oin.readObject();
		BigInteger e = (BigInteger) oin.readObject();
		return getPrivateKey(m, e);
	}

	public static PrivateKey getPrivateKey(BigInteger m, BigInteger e) throws NoSuchAlgorithmException, InvalidKeySpecException {
		RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
		KeyFactory fact = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = fact.generatePrivate(keySpec);
		return privateKey;
	}
	
	public static PublicKey getPublicKeyFromFile(InputStream keyFile) throws IOException, NoSuchAlgorithmException,
			InvalidKeySpecException, ClassNotFoundException {
		ObjectInputStream oin = new ObjectInputStream(new BufferedInputStream(keyFile));
		BigInteger m = (BigInteger) oin.readObject();
		BigInteger e = (BigInteger) oin.readObject();
		return getPublicKey(m, e);
	}

	public static PublicKey getPublicKey(BigInteger m, BigInteger e) throws NoSuchAlgorithmException, InvalidKeySpecException {
		RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
		KeyFactory fact = KeyFactory.getInstance("RSA");
		PublicKey publicKey = fact.generatePublic(keySpec);
		return publicKey;
	}
}
