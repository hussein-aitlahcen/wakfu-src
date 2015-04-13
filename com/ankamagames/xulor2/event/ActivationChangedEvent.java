package com.ankamagames.xulor2.event;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.event.*;

public class ActivationChangedEvent extends Event
{
    private boolean m_enabled;
    
    public ActivationChangedEvent(final Widget widget, final boolean enable) {
        super();
        this.m_target = widget;
        this.m_enabled = enable;
    }
    
    public boolean isEnabled() {
        return this.m_enabled;
    }
    
    public void setEnabled(final boolean enabled) {
        this.m_enabled = enabled;
    }
    
    @Override
    public Events getType() {
        return Events.ACTIVATION_CHANGED;
    }
}
