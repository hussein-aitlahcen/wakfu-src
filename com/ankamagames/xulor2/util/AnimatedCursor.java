package com.ankamagames.xulor2.util;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.xulor2.*;

public class AnimatedCursor implements Cursor, Runnable
{
    private static Logger m_logger;
    private java.awt.Cursor[] m_cursors;
    private int m_delay;
    private int m_offset;
    
    public AnimatedCursor(final java.awt.Cursor[] cursors, final int delay) {
        super();
        assert delay > 0 : "delay <= 0 !";
        assert cursors != null && cursors.length > 0 : "Invalid cursor array !";
        this.m_cursors = cursors;
        this.m_delay = delay;
        this.m_offset = 0;
    }
    
    public long getId() {
        return 1L;
    }
    
    public void setId(final long id) {
    }
    
    @Override
    public void show() {
        this.m_offset = 0;
        this.run();
        ProcessScheduler.getInstance().schedule(this, this.m_delay, -1);
    }
    
    @Override
    public void hide() {
        ProcessScheduler.getInstance().remove(this);
    }
    
    @Override
    public void run() {
        Xulor.getInstance().getAppUI().setCursor(this.m_cursors[this.m_offset]);
        this.m_offset = (this.m_offset + 1) % this.m_cursors.length;
    }
    
    static {
        AnimatedCursor.m_logger = Logger.getLogger((Class)AnimatedCursor.class);
    }
}
