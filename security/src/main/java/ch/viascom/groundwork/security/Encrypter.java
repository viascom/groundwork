package ch.viascom.groundwork.security;

import ch.viascom.groundwork.security.encryption.AES;
import ch.viascom.groundwork.security.handler.SecureServiceResultContentWriter;
import ch.viascom.groundwork.security.models.SecureServiceResultContent;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

/**
 * @author patrick.boesch@viascom.ch
 */
public class Encrypter {

    public static SecureServiceResultContent encrypt(Serializable input, PublicKey publicKey, byte[] iv, boolean toJson) throws NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, ShortBufferException, InvalidKeyException, IOException {
        SecureServiceResultContentWriter contentWriter = new SecureServiceResultContentWriter();
        SecureServiceResultContent content = contentWriter.createSecureContent(
                input, publicKey, AES.generateKeyOnly(), iv, toJson);
        return content;
    }
}
