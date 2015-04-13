package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.*;

public class UIMasterWorldFrame implements MessageFrame
{
    private static UIMasterWorldFrame m_instance;
    
    public static final UIMasterWorldFrame getInstance() {
        return UIMasterWorldFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        final WakfuGameEntity entity = WakfuGameEntity.getInstance();
        switch (message.getId()) {
            case 17681: {
                if (!entity.hasFrame(UICharacterSheetFrame.getInstance())) {
                    entity.pushFrame(UICharacterSheetFrame.getInstance());
                }
                return false;
            }
            case 17683: {
                if (entity.hasFrame(UICharacterSheetFrame.getInstance())) {
                    entity.removeFrame(UICharacterSheetFrame.getInstance());
                }
                else {
                    entity.pushFrame(UICharacterSheetFrame.getInstance());
                }
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
    
    static {
        UIMasterWorldFrame.m_instance = new UIMasterWorldFrame();
    }
}
