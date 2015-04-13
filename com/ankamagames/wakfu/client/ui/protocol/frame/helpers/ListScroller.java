package com.ankamagames.wakfu.client.ui.protocol.frame.helpers;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;

public class ListScroller implements Runnable
{
    private static final int INTERVAL = 20;
    private static final float SCROLL_DURATION = 250.0f;
    private final List m_list;
    private long m_lastScrollTime;
    private ScrollMode m_scrollMode;
    
    public ListScroller(final List list) {
        super();
        this.m_list = list;
        this.m_scrollMode = ScrollMode.STOPPED;
    }
    
    public void start() {
        ProcessScheduler.getInstance().schedule(this, 20L, -1);
    }
    
    public void stop() {
        ProcessScheduler.getInstance().remove(this);
    }
    
    public void setScrollMode(final ScrollMode scrollMode) {
        this.m_scrollMode = scrollMode;
    }
    
    @Override
    public void run() {
        if (this.m_list == null) {
            return;
        }
        final long now = System.nanoTime();
        final long deltaTime = (now - this.m_lastScrollTime) / 1000000L;
        this.m_lastScrollTime = now;
        if (this.m_scrollMode == ScrollMode.STOPPED) {
            return;
        }
        final float offset = this.m_list.getOffset();
        final float deltaOffset = deltaTime / 250.0f;
        final float finalOffset = (this.m_scrollMode == ScrollMode.LEFT) ? (offset - deltaOffset) : (offset + deltaOffset);
        this.m_list.setOffset(finalOffset);
    }
    
    public enum ScrollMode
    {
        STOPPED, 
        LEFT, 
        RIGHT;
    }
}
