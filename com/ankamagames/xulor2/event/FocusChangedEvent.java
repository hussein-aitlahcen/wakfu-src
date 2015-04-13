package com.ankamagames.xulor2.event;

import org.apache.log4j.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.event.*;

public class FocusChangedEvent extends Event
{
    private static Logger m_logger;
    private boolean m_focused;
    private EventDispatcher m_widgetClicked;
    
    public FocusChangedEvent(final Widget target, final boolean focused) {
        super();
        this.m_target = target;
        this.m_focused = focused;
    }
    
    public void setFocused(final boolean focused) {
        this.m_focused = focused;
    }
    
    public boolean getFocused() {
        return this.m_focused;
    }
    
    public EventDispatcher getWidgetClicked() {
        return this.m_widgetClicked;
    }
    
    @Override
    public Events getType() {
        return Events.FOCUS_CHANGED;
    }
    
    static {
        FocusChangedEvent.m_logger = Logger.getLogger((Class)FocusChangedEvent.class);
    }
}
