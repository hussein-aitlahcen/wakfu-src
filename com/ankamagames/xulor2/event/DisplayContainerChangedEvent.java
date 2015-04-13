package com.ankamagames.xulor2.event;

import org.apache.log4j.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.event.*;

public class DisplayContainerChangedEvent extends Event
{
    private static Logger m_logger;
    private boolean m_full;
    
    public DisplayContainerChangedEvent(final Widget target, final boolean full) {
        super();
        this.m_full = false;
        this.m_target = target;
        this.m_full = full;
    }
    
    public boolean isFull() {
        return this.m_full;
    }
    
    @Override
    public Events getType() {
        return Events.DISPLAY_CONTAINER_CHANGED;
    }
    
    static {
        DisplayContainerChangedEvent.m_logger = Logger.getLogger((Class)DisplayContainerChangedEvent.class);
    }
}
