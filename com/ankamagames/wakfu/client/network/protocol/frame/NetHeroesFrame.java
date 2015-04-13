package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

public class NetHeroesFrame implements MessageFrame
{
    private static final Logger m_logger;
    public static final NetHeroesFrame INSTANCE;
    
    @Override
    public boolean onMessage(final Message message) {
        return true;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded && !WakfuGameEntity.getInstance().hasFrame(UIHeroesFrame.INSTANCE)) {
            WakfuGameEntity.getInstance().pushFrame(UIHeroesFrame.INSTANCE);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved && WakfuGameEntity.getInstance().hasFrame(UIHeroesFrame.INSTANCE)) {
            WakfuGameEntity.getInstance().removeFrame(UIHeroesFrame.INSTANCE);
        }
    }
    
    @Override
    public long getId() {
        return 1L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetHeroesFrame.class);
        INSTANCE = new NetHeroesFrame();
    }
}
