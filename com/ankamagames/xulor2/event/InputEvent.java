package com.ankamagames.xulor2.event;

import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.kernel.utils.*;

public abstract class InputEvent extends Event
{
    protected int m_modifiers;
    private static final int ALT_MASK = 512;
    private static final int ALT_GRAPH_MASK = 8192;
    private static final int SHIFT_MASK = 64;
    private static final int CTRL_MASK = 128;
    private static final int META_MASK = 256;
    
    public int getModifiers() {
        return this.m_modifiers;
    }
    
    public void setModifiers(final int modifiers) {
        this.m_modifiers = modifiers;
    }
    
    public boolean hasCtrl() {
        final boolean ctrlDown = (this.m_modifiers & 0x80) == 0x80;
        final boolean metaDown = (this.m_modifiers & 0x100) == 0x100;
        return ctrlDown || (OS.isMacOs() && metaDown);
    }
    
    public boolean hasAlt() {
        return (this.m_modifiers & 0x200) == 0x200;
    }
    
    public boolean hasAltGraph() {
        return (this.m_modifiers & 0x2000) == 0x2000;
    }
    
    public boolean hasShift() {
        return (this.m_modifiers & 0x40) == 0x40;
    }
    
    public boolean hasMeta() {
        return (this.m_modifiers & 0x100) == 0x100;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_modifiers = 0;
    }
}
