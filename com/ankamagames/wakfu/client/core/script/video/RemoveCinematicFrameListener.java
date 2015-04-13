package com.ankamagames.wakfu.client.core.script.video;

import com.ankamagames.framework.video.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;

public class RemoveCinematicFrameListener implements VideoEventListener
{
    @Override
    public void onVideoEnd() {
        WakfuGameEntity.getInstance().removeFrame(UICinematicVideoFrame.INSTANCE);
    }
    
    @Override
    public void onVideoStopped() {
    }
    
    @Override
    public void onVideoOutput() {
    }
    
    @Override
    public void onBuffering(final float buffering) {
    }
}
