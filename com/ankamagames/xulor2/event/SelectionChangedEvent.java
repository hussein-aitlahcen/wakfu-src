package com.ankamagames.xulor2.event;

import org.apache.log4j.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.event.*;

public class SelectionChangedEvent extends Event
{
    private static Logger m_logger;
    private boolean m_selected;
    
    public SelectionChangedEvent(final EventDispatcher e, final boolean b) {
        super();
        this.m_selected = false;
        this.m_selected = b;
        this.m_target = e;
    }
    
    public boolean isSelected() {
        return this.m_selected;
    }
    
    public void setSelected(final boolean selected) {
        this.m_selected = selected;
    }
    
    @Override
    public Events getType() {
        return Events.SELECTION_CHANGED;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_selected = false;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
    }
    
    static {
        SelectionChangedEvent.m_logger = Logger.getLogger((Class)SelectionChangedEvent.class);
    }
}
