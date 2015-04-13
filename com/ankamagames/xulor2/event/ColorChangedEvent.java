package com.ankamagames.xulor2.event;

import org.apache.log4j.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.core.event.*;

public class ColorChangedEvent extends Event
{
    private static Logger m_logger;
    
    public ColorChangedEvent(final ColorElement c) {
        super();
        this.m_target = c;
    }
    
    public ColorElement getColorElement() {
        return (ColorElement)this.m_target;
    }
    
    public void setColorElement(final ColorElement color) {
        this.m_target = color;
    }
    
    @Override
    public Events getType() {
        return Events.COLOR_CHANGED;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
    }
    
    static {
        ColorChangedEvent.m_logger = Logger.getLogger((Class)ColorChangedEvent.class);
    }
}
