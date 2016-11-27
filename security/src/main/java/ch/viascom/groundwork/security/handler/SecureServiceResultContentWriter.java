package ch.viascom.groundwork.security.handler;

import ch.viascom.groundwork.security.encryption.AES;
import ch.viascom.groundwork.security.encryption.RSA;
import ch.viascom.groundwork.security.models.SecureServiceResultContent;
import com.google.gson.Gson;
import org.apache.commons.lang3.SerializationUtils;

import javax.crypto.*;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Base64;

public class SecureServiceResultContentWriter {

    /**
     * 
     * 
     * String test = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr";
     * byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
     * 
     * InputStream in = SecureTest.class.getClassLoader().getResourceAsStream(
     * "public_server.key");
     * 
     * SecureServiceResultContentWriter contentWriter = new
     * SecureServiceResultContentWriter();
     * SecureServiceResultContent content =
     * contentWriter.createSecureContent(test,
     * RSAKeyLoader.getPublicKeyFromFile(in), AES.generateKeyOnly(), iv);
     * 
     * ServiceResult&lt;SecureServiceResultContent&gt; serviceResult = new
     * ServiceResult&lt;&gt;();
     * serviceResult.setContent(content).setStatus(ServiceResultStatus.
     * successful);
     * 
     * return serviceResult;
     * 
     * 
     * @param content
     * @param publicKey
     * @param secretKey
     * @param iv
     * @return
     * @throws IOException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidAlgorithmParameterException
     * @throws ShortBufferException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public SecureServiceResultContent createSecureContent(Serializable content, PublicKey publicKey, SecretKey secretKey, byte[] iv, boolean toJson)
            throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException,
            ShortBufferException, IllegalBlockSizeException, BadPaddingException {

        AES aes = new AES();
        RSA rsa = new RSA();

        // Encrypt content
        byte[] byteContent;
        if (toJson) {
            byteContent = objectToJsonByteArray(content);
        } else {
            byteContent = objectToByteArray(content);
        }
        byte[] encrypted = aes.generateSecretKeySpec(secretKey).generateIvParameterSpec(iv).encrypt(byteContent);
        String encryptedOutput = Base64.getEncoder().encodeToString(encrypted);

        // Encrypt key
        String keyOutput = Base64.getEncoder().encodeToString(rsa.setPublicKey(publicKey).rsaEncrypt(secretKey.getEncoded()));

        // Encrypt iv
        String ivOutput = Base64.getEncoder().encodeToString(rsa.setPublicKey(publicKey).rsaEncrypt(iv));

        // new SecureServiceResultContent
        SecureServiceResultContent secureServiceResultContent = new SecureServiceResultContent();
        secureServiceResultContent.setSecureContent(encryptedOutput);
        secureServiceResultContent.setSecureKey(keyOutput);
        secureServiceResultContent.setSecureIV(ivOutput);
        secureServiceResultContent.setSecureContentClass(content.getClass().getSimpleName());

        return secureServiceResultContent;
    }

    public SecureServiceResultContent createSecureContent(Serializable content, PublicKey publicKey, SecretKey secretKey, byte[] iv)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, ShortBufferException,
            IllegalBlockSizeException, BadPaddingException, IOException {
        return createSecureContent(content, publicKey, secretKey, iv, false);
    }
    
    private byte[] objectToJsonByteArray(Serializable o){
        Gson gson = new Gson();
        String json = gson.toJson(o);
        return json.getBytes();
    }

    private byte[] objectToByteArray(Serializable o) throws IOException {
        return SerializationUtils.serialize(o);
    }
}
