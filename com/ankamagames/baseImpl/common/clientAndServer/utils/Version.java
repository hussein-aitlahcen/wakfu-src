package com.ankamagames.baseImpl.common.clientAndServer.utils;

import org.apache.log4j.*;

public abstract class Version
{
    protected static final Logger m_logger;
    private static Version m_uniqueVersionChecker;
    
    protected Version() {
        super();
        Version.m_uniqueVersionChecker = this;
    }
    
    public static boolean checkVersion(final byte[] datas) {
        if (Version.m_uniqueVersionChecker != null) {
            return Version.m_uniqueVersionChecker.implCheckVersion(datas);
        }
        Version.m_logger.error((Object)"Le v\u00e9rificateur de version n'a pas \u00e9t\u00e9 d\u00e9finit");
        return false;
    }
    
    public static byte[] getNeededVersion() {
        if (Version.m_uniqueVersionChecker != null) {
            return Version.m_uniqueVersionChecker.implGetNeededVersion();
        }
        Version.m_logger.error((Object)"Le v\u00e9rificateur de version n'a pas \u00e9t\u00e9 d\u00e9finit");
        return new byte[0];
    }
    
    protected abstract boolean implCheckVersion(final byte[] p0);
    
    protected abstract byte[] implGetNeededVersion();
    
    static {
        m_logger = Logger.getLogger((Class)Version.class);
    }
}
