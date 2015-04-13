package com.ankamagames.xulor2.event;

import org.apache.log4j.*;
import com.ankamagames.xulor2.appearance.spacing.*;
import com.ankamagames.xulor2.core.event.*;

public class SpacingChangedEvent extends Event
{
    private static Logger m_logger;
    
    public SpacingChangedEvent(final InsetsElement spacing) {
        super();
        this.m_target = spacing;
    }
    
    public InsetsElement getSpacing() {
        return (InsetsElement)this.m_target;
    }
    
    public void setSpacing(final InsetsElement spacing) {
        this.m_target = spacing;
    }
    
    @Override
    public Events getType() {
        return Events.SPACING_CHANGED;
    }
    
    static {
        SpacingChangedEvent.m_logger = Logger.getLogger((Class)SpacingChangedEvent.class);
    }
}
