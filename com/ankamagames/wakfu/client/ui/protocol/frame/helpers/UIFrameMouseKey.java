package com.ankamagames.wakfu.client.ui.protocol.frame.helpers;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public abstract class UIFrameMouseKey
{
    private static final Logger m_logger;
    
    public static boolean isKeyMessage(final Message message) {
        return message.getId() == 19990 || message.getId() == 19991;
    }
    
    public static boolean isMouseMessage(final Message message) {
        return message.getId() == 19994 || message.getId() == 19995 || message.getId() == 19998 || message.getId() == 19992 || message.getId() == 19995 || message.getId() == 19997;
    }
    
    public static boolean isKeyOrMouseMessage(final Message message) {
        return isMouseMessage(message) || isKeyMessage(message);
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIFrameMouseKey.class);
    }
}
