package com.ankamagames.xulor2.event;

import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.event.*;

public class WindowStickEvent extends Event
{
    private Alignment9 m_align;
    
    public WindowStickEvent(final Window target, final Alignment9 align) {
        super();
        this.m_target = target;
        this.m_align = align;
    }
    
    public Alignment9 getAlign() {
        return this.m_align;
    }
    
    @Override
    public Events getType() {
        return Events.WINDOW_STICK;
    }
    
    @Override
    public void onCheckOut() {
        super.onCheckOut();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_align = null;
    }
}
