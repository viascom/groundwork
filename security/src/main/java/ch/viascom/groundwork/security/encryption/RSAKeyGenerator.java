package ch.viascom.groundwork.security.encryption;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import lombok.Getter;

@Getter
public class RSAKeyGenerator {
	KeyPair kp;
	Key publicKey;
	Key privateKey;

	RSAPublicKeySpec pub;
	RSAPrivateKeySpec priv;

	private RSAKeyGenerator generateKeys() throws NoSuchAlgorithmException {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(2048);
		kp = kpg.genKeyPair();
		publicKey = kp.getPublic();
		privateKey = kp.getPrivate();
		return this;
	}

	public RSAKeyGenerator generateKeySpec() throws InvalidKeySpecException, NoSuchAlgorithmException {
		generateKeys();
		KeyFactory fact = KeyFactory.getInstance("RSA");
		pub = fact.getKeySpec(kp.getPublic(), RSAPublicKeySpec.class);
		priv = fact.getKeySpec(kp.getPrivate(), RSAPrivateKeySpec.class);
		return this;
	}
	
	public void saveKeysToFile(String suffix) throws IOException {
		saveToFile("public" + suffix + ".key", pub.getModulus(), pub.getPublicExponent());
		saveToFile("private" + suffix + ".key", priv.getModulus(), priv.getPrivateExponent());
	}

	private void saveToFile(String fileName, BigInteger mod, BigInteger exp) throws IOException {
		ObjectOutputStream oout = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
		try {
			oout.writeObject(mod);
			oout.writeObject(exp);
		} catch (Exception e) {
			throw new IOException("Unexpected error", e);
		} finally {
			oout.close();
		}
	}
}
