package com.ankamagames.xulor2.event;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.event.*;

public class WidgetRemovalRequestedEvent extends Event
{
    public WidgetRemovalRequestedEvent(final Widget target) {
        super();
        this.m_target = target;
    }
    
    @Override
    public Events getType() {
        return Events.WIDGET_REMOVAL_REQUESTED;
    }
}
