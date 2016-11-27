package ch.viascom.groundwork.security.handler;

import ch.viascom.groundwork.security.encryption.AES;
import ch.viascom.groundwork.security.encryption.RSA;
import ch.viascom.groundwork.security.models.SecureServiceResultContent;
import com.google.gson.Gson;
import org.apache.commons.lang3.SerializationUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Base64;

public class SecureServiceResultContentReader {

    /**
     * String contentString = args[0];
     * String keyString = args[1];
     * String ivString = args[2];
     * 
     * SecureServiceResultContentReader contentReader = new
     * SecureServiceResultContentReader();
     * 
     * InputStream in =
     * Main.class.getClassLoader().getResourceAsStream("private_server.key");
     * 
     * SecureServiceResultContent secureContent = new
     * SecureServiceResultContent();
     * 
     * secureContent.setSecureContent(contentString).setSecureKey(keyString).
     * setSecureIV(ivString);
     * 
     * Object test = contentReader.readSecureContent(secureContent,
     * RSAKeyLoader.getPrivateKeyFromFile(in));
     * 
     * 
     * @param secureContent
     * @param privateKey
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws IOException
     * @throws InvalidAlgorithmParameterException
     * @throws ShortBufferException
     */
    public Object readSecureContent(SecureServiceResultContent secureContent, PrivateKey privateKey, boolean fromJson) throws InvalidKeyException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidAlgorithmParameterException,
            ShortBufferException {

        AES aes = new AES();
        RSA rsa = new RSA();

        String contentString = secureContent.getSecureContent();
        String keyString = secureContent.getSecureKey();
        String ivString = secureContent.getSecureIV();

        // Decrypt key
        byte[] secretKeyByteArray = rsa.setPrivateKey(privateKey).rsaDecrypt(Base64.getDecoder().decode(keyString));

        // Decode iv
        byte[] iv = rsa.setPrivateKey(privateKey).rsaDecrypt(Base64.getDecoder().decode(ivString));

        // Decode content
        byte[] contentByteArray = aes.setSecretKeySpecFromByteArray(secretKeyByteArray).generateIvParameterSpec(iv)
                .decrypt(Base64.getDecoder().decode(contentString));
        if (fromJson) {
            
            return jsonByteArraytoObject(contentByteArray);
        } else {
            return byteArraytoObject(contentByteArray);
        }
    }

    public Object readSecureContent(SecureServiceResultContent secureContent, PrivateKey privateKey) throws InvalidKeyException, NoSuchAlgorithmException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException, InvalidAlgorithmParameterException, ShortBufferException {
        return readSecureContent(secureContent, privateKey, false);
    }

    private Object jsonByteArraytoObject(byte[] bytes) throws IOException {
        Gson gson = new Gson();
        Object obj = gson.fromJson(new String(bytes), Object.class);
        return obj;
    }
    
    private Object byteArraytoObject(byte[] bytes) throws IOException {
        return SerializationUtils.deserialize(bytes);
    }
}
