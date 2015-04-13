package com.ankamagames.framework.fileFormat.io.binaryStorage;

import org.apache.log4j.*;

public final class SimpleBinaryStorage extends AbstractSimpleBinaryStorage
{
    private static final Logger m_logger;
    private static final SimpleBinaryStorage m_instance;
    
    public static SimpleBinaryStorage getInstance() {
        return SimpleBinaryStorage.m_instance;
    }
    
    private SimpleBinaryStorage() {
        super("data.bdat", "indexes.bdat", true);
        this.setName("SimpleBinaryStorage");
    }
    
    public SimpleBinaryStorage(final String name) {
        super("data.bdat", "indexes.bdat", true);
        this.setName(name);
    }
    
    static {
        m_logger = Logger.getLogger((Class)SimpleBinaryStorage.class);
        m_instance = new SimpleBinaryStorage();
    }
}
