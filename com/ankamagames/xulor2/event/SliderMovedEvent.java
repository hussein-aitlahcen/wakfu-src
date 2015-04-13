package com.ankamagames.xulor2.event;

import org.apache.log4j.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.event.*;

public class SliderMovedEvent extends Event
{
    private static Logger m_logger;
    private float m_value;
    
    public SliderMovedEvent(final Widget target) {
        super();
        this.m_value = 0.0f;
        this.m_target = target;
    }
    
    public void setValue(final float value) {
        this.m_value = value;
    }
    
    public float getValue() {
        return this.m_value;
    }
    
    @Override
    public Events getType() {
        return Events.SLIDER_MOVED;
    }
    
    static {
        SliderMovedEvent.m_logger = Logger.getLogger((Class)SliderMovedEvent.class);
    }
}
