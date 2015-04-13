package com.ankamagames.wakfu.client.ui.protocol.message.shortcut;

import com.ankamagames.wakfu.client.ui.protocol.message.*;

public class UIShortcutMessage extends UIMessage
{
    private Object m_item;
    private int m_shorcutBarNumber;
    private int m_position;
    private byte m_previousBar;
    private int m_previousPosition;
    private boolean m_force;
    
    public UIShortcutMessage() {
        super();
        this.m_previousBar = -1;
        this.m_previousPosition = -1;
    }
    
    public Object getItem() {
        return this.m_item;
    }
    
    public void setItem(final Object item) {
        this.m_item = item;
    }
    
    public int getShorcutBarNumber() {
        return this.m_shorcutBarNumber;
    }
    
    public void setShorcutBarNumber(final int shorcutBarNumber) {
        this.m_shorcutBarNumber = shorcutBarNumber;
    }
    
    public int getPosition() {
        return this.m_position;
    }
    
    public void setPosition(final int position) {
        this.m_position = position;
    }
    
    public byte getPreviousBar() {
        return this.m_previousBar;
    }
    
    public void setPreviousBar(final byte previousBar) {
        this.m_previousBar = previousBar;
    }
    
    public int getPreviousPosition() {
        return this.m_previousPosition;
    }
    
    public void setPreviousPosition(final int previousPosition) {
        this.m_previousPosition = previousPosition;
    }
    
    public boolean isForce() {
        return this.m_force;
    }
    
    public void setForce(final boolean force) {
        this.m_force = force;
    }
}
