package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.wakfu.client.core.game.challenge.*;
import com.ankamagames.framework.kernel.*;

public class UIChallengeFrame implements MessageFrame
{
    private static final Logger m_logger;
    private static UIChallengeFrame m_instance;
    
    public static UIChallengeFrame getInstance() {
        return UIChallengeFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (message instanceof ClockMessage) {
            final ClockMessage clockMessage = (ClockMessage)message;
            if (clockMessage.getSubId() == -1) {
                AreaChallengeWaiting.INSTANCE.updateTime();
            }
            return false;
        }
        message.getId();
        return true;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            return;
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            return;
        }
    }
    
    @Override
    public long getId() {
        return 5L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIChallengeFrame.class);
        UIChallengeFrame.m_instance = new UIChallengeFrame();
    }
}
