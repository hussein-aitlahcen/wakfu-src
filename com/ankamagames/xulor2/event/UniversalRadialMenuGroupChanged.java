package com.ankamagames.xulor2.event;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.event.*;

public class UniversalRadialMenuGroupChanged extends Event
{
    private int m_group;
    
    public UniversalRadialMenuGroupChanged(final Widget target, final int group) {
        super();
        this.m_target = target;
        this.m_group = group;
    }
    
    @Override
    public Events getType() {
        return Events.UNIVERSAL_RADIAL_MENU_GROUP_CHANGED;
    }
    
    public int getGroup() {
        return this.m_group;
    }
}
