package com.ankamagames.wakfu.client.network.security;

import com.ankamagames.wakfu.common.security.*;
import java.security.spec.*;
import java.security.*;
import javax.crypto.*;
import com.ankamagames.wakfu.common.constants.*;

public class ConnectionEncryptionManager extends AbstractEncryptionManager
{
    public static final ConnectionEncryptionManager INSTANCE;
    private KeyFactory m_keyFactory;
    
    public ConnectionEncryptionManager(final String algorithm, final AlgorithmParameterSpec algorithmParameters) {
        super(algorithm, algorithmParameters);
        try {
            this.m_keyFactory = KeyFactory.getInstance(this.m_algorithm);
        }
        catch (NoSuchAlgorithmException e) {
            ConnectionEncryptionManager.m_logger.fatal((Object)("Algorithme non support\u00e9 : " + this.m_algorithm), (Throwable)e);
        }
    }
    
    public void initialize(final byte[] rawPublicKey) {
        try {
            final EncodedKeySpec encodedKeySpec = this.getEncodedKeySpec(rawPublicKey);
            final PublicKey publicKey = this.m_keyFactory.generatePublic(encodedKeySpec);
            this.m_cipher.init(1, publicKey);
        }
        catch (InvalidKeySpecException e) {
            ConnectionEncryptionManager.m_logger.fatal((Object)("Spec de clef publique invalide : " + this.m_algorithm), (Throwable)e);
        }
        catch (InvalidKeyException e2) {
            ConnectionEncryptionManager.m_logger.fatal((Object)("Clef publique invalide : " + this.m_algorithm), (Throwable)e2);
        }
    }
    
    @Override
    public byte[] crypt(final byte[] rawData) {
        try {
            return this.m_cipher.doFinal(rawData);
        }
        catch (IllegalBlockSizeException e) {
            ConnectionEncryptionManager.m_logger.error((Object)"taille de bloc invalide", (Throwable)e);
        }
        catch (BadPaddingException e2) {
            ConnectionEncryptionManager.m_logger.error((Object)"padding invalide", (Throwable)e2);
        }
        return null;
    }
    
    @Override
    public byte[] decrypt(final byte[] encryptedData) {
        throw new UnsupportedOperationException("le client ne doit pas decrypter de donn\u00e9es de connection");
    }
    
    static {
        INSTANCE = new ConnectionEncryptionManager("RSA", ConnectionEncryptionConstants.ALGORITHM_PARAMETERS);
    }
}
