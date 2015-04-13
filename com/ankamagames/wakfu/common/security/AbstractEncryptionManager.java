package com.ankamagames.wakfu.common.security;

import org.apache.log4j.*;
import java.security.*;
import javax.crypto.*;
import java.security.spec.*;

public abstract class AbstractEncryptionManager
{
    protected static Logger m_logger;
    protected final String m_algorithm;
    protected final AlgorithmParameterSpec m_algorithmParameters;
    protected Cipher m_cipher;
    
    protected AbstractEncryptionManager(final String algorithm, final AlgorithmParameterSpec algorithmParameters) {
        super();
        this.m_algorithm = algorithm;
        this.m_algorithmParameters = algorithmParameters;
        try {
            this.m_cipher = Cipher.getInstance(this.m_algorithm);
        }
        catch (NoSuchAlgorithmException e) {
            AbstractEncryptionManager.m_logger.fatal((Object)("Algorithme non support\u00e9 : " + this.m_algorithm), (Throwable)e);
        }
        catch (NoSuchPaddingException e2) {
            AbstractEncryptionManager.m_logger.fatal((Object)("Padding non support\u00e9 pour " + this.m_algorithm), (Throwable)e2);
        }
    }
    
    protected EncodedKeySpec getEncodedKeySpec(final byte[] encodedKey) {
        return new X509EncodedKeySpec(encodedKey);
    }
    
    public String getAlgorithm() {
        return this.m_algorithm;
    }
    
    public abstract byte[] crypt(final byte[] p0);
    
    public abstract byte[] decrypt(final byte[] p0);
    
    static {
        AbstractEncryptionManager.m_logger = Logger.getLogger((Class)AbstractEncryptionManager.class);
    }
}
