package ch.viascom.groundwork.foxhttp.ssl;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

/**
 * @author patrick.boesch@viascom.ch
 */
public class OnwSSLCertificateTrustStrategy implements FoxHttpSSLTrustStrategy {

    private InputStream keyStoreInputStream;
    private String JKSPassword;
    private String KEYPassword;

    public OnwSSLCertificateTrustStrategy(InputStream keyStoreInputStream, String JKSPassword, String KEYPassword) {
        this.keyStoreInputStream = keyStoreInputStream;
        this.JKSPassword = JKSPassword;
        this.KEYPassword = KEYPassword;
    }

    @Override
    public SSLSocketFactory getSSLSocketFactory(HttpsURLConnection httpsURLConnection) {
        final char[] JKS_PASSWORD = JKSPassword.toCharArray();
        final char[] KEY_PASSWORD = KEYPassword.toCharArray();
        try {
            /* Get the JKS contents */
            final KeyStore keyStore = KeyStore.getInstance("JKS");
            try (final InputStream is = keyStoreInputStream) {
                keyStore.load(is, JKS_PASSWORD);
            }
            final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
                    .getDefaultAlgorithm());
            kmf.init(keyStore, KEY_PASSWORD);
            final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory
                    .getDefaultAlgorithm());
            tmf.init(keyStore);

            /*
             * Creates a socket factory for HttpsURLConnection using JKS
             * contents
             */
            final SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new java.security.SecureRandom());
            final SSLSocketFactory socketFactory = sc.getSocketFactory();
            return socketFactory;

        } catch (final GeneralSecurityException | IOException exc) {
            throw new RuntimeException(exc);
        }
    }
}
