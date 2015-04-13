package com.ankamagames.framework.fileFormat;

import org.apache.log4j.*;

public final class FileLoadingLogger
{
    private static final Logger m_loadingLogger;
    private static final boolean ACTIVATED = true;
    
    public static void logUsageOf(final String path) {
        FileLoadingLogger.m_loadingLogger.info((Object)path);
    }
    
    static {
        m_loadingLogger = Logger.getLogger("fileLoadingLogger");
    }
}
