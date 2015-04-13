package com.ankamagames.wakfu.client.core.script.video;

import com.ankamagames.framework.video.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.*;

public class changeSpuListener implements VideoEventListener
{
    private final VideoWidget m_widget;
    
    public changeSpuListener(final VideoWidget widget) {
        super();
        this.m_widget = widget;
    }
    
    @Override
    public void onVideoEnd() {
    }
    
    @Override
    public void onVideoStopped() {
    }
    
    @Override
    public void onBuffering(final float buffering) {
    }
    
    @Override
    public void onVideoOutput() {
        this.m_widget.setSpu(VideoSpu.fromLanguage(WakfuTranslator.getInstance().getLanguage()).m_trackIdx);
    }
}
