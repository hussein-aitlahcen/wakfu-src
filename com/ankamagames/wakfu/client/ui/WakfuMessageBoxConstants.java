package com.ankamagames.wakfu.client.ui;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;

public class WakfuMessageBoxConstants
{
    private static final Logger m_logger;
    public static final int QUESTION = 0;
    public static final int WARNING = 1;
    public static final int EXCHANGE = 2;
    public static final int PARTY = 3;
    public static final int GUILD = 4;
    public static final int FIGHT = 5;
    public static final int TRAINING = 6;
    public static final int INFO = 7;
    public static final int HAVEN_WORLD = 8;
    
    public static String getMessageBoxIconUrl(final int iconId) {
        try {
            return String.format(WakfuConfiguration.getInstance().getString("messageBoxIconsPath"), iconId);
        }
        catch (PropertyException e) {
            WakfuMessageBoxConstants.m_logger.warn((Object)e.getMessage(), (Throwable)e);
            return null;
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuMessageBoxConstants.class);
    }
}
