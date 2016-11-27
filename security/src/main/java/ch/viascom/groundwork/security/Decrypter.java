package ch.viascom.groundwork.security;

import ch.viascom.groundwork.security.handler.SecureServiceResultContentReader;
import ch.viascom.groundwork.security.models.SecureServiceResultContent;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

/**
 * @author patrick.boesch@viascom.ch
 */
public class Decrypter {
    public static Object decryptor(SecureServiceResultContent input, PrivateKey privateKey, boolean fromJson) throws NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, ShortBufferException, InvalidKeyException, IOException {
        SecureServiceResultContentReader contentReader = new SecureServiceResultContentReader();
        Object output = contentReader.readSecureContent(input, privateKey, fromJson);
        return output;
    }
}
