package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;

public class NetCalendarFrame implements MessageFrame
{
    protected static final Logger m_logger;
    public static final NetCalendarFrame INSTANCE;
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 2063: {
                final ClientCalendarSynchronizationMessage msg = (ClientCalendarSynchronizationMessage)message;
                final long stamp = System.currentTimeMillis() - msg.getWorkerTimeStamp();
                WakfuGameCalendar.getInstance().synchronize(msg.getSynchronizationTime() + stamp);
                WakfuGameCalendar.getInstance().start(1000L);
                WakfuGameCalendar.getInstance().run();
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetCalendarFrame.class);
        INSTANCE = new NetCalendarFrame();
    }
}
