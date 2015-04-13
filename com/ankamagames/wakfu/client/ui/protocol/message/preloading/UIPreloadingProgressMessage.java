package com.ankamagames.wakfu.client.ui.protocol.message.preloading;

import com.ankamagames.wakfu.client.ui.protocol.message.*;

public class UIPreloadingProgressMessage extends UIMessage
{
    private double m_percent;
    private int m_estimatedTime;
    
    public UIPreloadingProgressMessage(final short id, final double percent, final int estimatedTime) {
        super(id);
        this.m_percent = percent;
        this.m_estimatedTime = estimatedTime;
    }
    
    public double getPercent() {
        return this.m_percent;
    }
    
    public int getEstimatedTime() {
        return this.m_estimatedTime;
    }
}
