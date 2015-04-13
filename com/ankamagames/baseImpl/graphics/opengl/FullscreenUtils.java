package com.ankamagames.baseImpl.graphics.opengl;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;

public class FullscreenUtils
{
    private static final Logger m_logger;
    public static final FullscreenUtils INSTANCE;
    
    public native void showMenuAndDock(final boolean p0);
    
    static {
        m_logger = Logger.getLogger((Class)FullscreenUtils.class);
        try {
            if (OS.getCurrentOS() == OS.MAC) {
                System.loadLibrary("fullscreenutils");
            }
        }
        catch (Throwable e) {
            FullscreenUtils.m_logger.error((Object)"Impossible d'afficher le menu et le doc Mac", e);
        }
        INSTANCE = new FullscreenUtils();
    }
}
