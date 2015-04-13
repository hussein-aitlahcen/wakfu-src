package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;

public class UIMessageCatcherFrame implements MessageFrame
{
    public static final UIMessageCatcherFrame INSTANCE;
    
    @Override
    public boolean onMessage(final Message message) {
        return !UIFrameMouseKey.isMouseMessage(message);
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded && WakfuGameEntity.getInstance().hasFrame(UIWorldInteractionFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIWorldInteractionFrame.getInstance());
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        INSTANCE = new UIMessageCatcherFrame();
    }
}
