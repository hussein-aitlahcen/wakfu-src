package com.ankamagames.wakfu.common.game.aptitude;

import org.apache.log4j.*;

public final class AptitudeComposer
{
    protected static final Logger m_logger;
    private static AptitudeDisplayer m_aptitudesFieldProvider;
    
    public static void init(final AptitudeDisplayer aptitudeDisplayer) {
        AptitudeComposer.m_aptitudesFieldProvider = aptitudeDisplayer;
    }
    
    public static AptitudeDisplayer fieldProvider() {
        return AptitudeComposer.m_aptitudesFieldProvider;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AptitudeComposer.class);
    }
}
