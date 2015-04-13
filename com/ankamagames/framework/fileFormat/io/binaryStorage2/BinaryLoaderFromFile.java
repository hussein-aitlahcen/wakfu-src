package com.ankamagames.framework.fileFormat.io.binaryStorage2;

import org.apache.log4j.*;

public final class BinaryLoaderFromFile<T extends BinaryData> implements BinaryLoader<T>
{
    private static final Logger m_logger;
    private final T m_binary;
    
    public BinaryLoaderFromFile(final T binary) {
        super();
        this.m_binary = binary;
    }
    
    @Override
    public T createFromId(final int id) {
        try {
            if (BinaryDocumentManager.getInstance().getId(id, this.m_binary)) {
                return this.m_binary;
            }
        }
        catch (Exception e) {
            BinaryLoaderFromFile.m_logger.error((Object)("Probl\u00e8me avec " + this.m_binary.getClass().getSimpleName() + " d'id=" + id), (Throwable)e);
        }
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)BinaryLoaderFromFile.class);
    }
}
