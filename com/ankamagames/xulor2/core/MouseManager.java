package com.ankamagames.xulor2.core;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.xulor2.core.event.*;

public class MouseManager implements MessageHandler
{
    public static final int DEFAULT_DOUBLE_CLICK_LATENCY = 300;
    protected static final MouseManager m_manager;
    private int m_x;
    private int m_y;
    private int m_doubleClickLatency;
    private Widget m_componentPressed;
    private long m_id;
    private int m_buttonPressed;
    
    private MouseManager() {
        super();
        this.m_doubleClickLatency = 300;
        this.m_id = this.hashCode();
        MessageScheduler.getInstance().start();
    }
    
    public static MouseManager getInstance() {
        return MouseManager.m_manager;
    }
    
    public void setXY(final int x, final int y) {
        this.m_x = x;
        this.m_y = y;
    }
    
    public int getX() {
        return this.m_x;
    }
    
    public int getY() {
        return this.m_y;
    }
    
    @Override
    public long getId() {
        return this.m_id;
    }
    
    @Override
    public void setId(final long id) {
        this.m_id = id;
    }
    
    public int getDoubleClickLatency() {
        return this.m_doubleClickLatency;
    }
    
    public void setDoubleClickLatency(final int doubleClickLatency) {
        this.m_doubleClickLatency = doubleClickLatency;
    }
    
    public void notifyPressed(final Widget component, final MouseEvent event) {
        MessageScheduler.getInstance().removeAllClocks(this);
        if (this.m_componentPressed == component && this.m_buttonPressed == event.getButton()) {
            this.simpleClick(component, MouseEvent.checkOut(event));
            this.doubleClick(component, MouseEvent.checkOut(event));
            this.m_componentPressed = null;
        }
        else {
            this.m_componentPressed = component;
            this.m_buttonPressed = event.getButton();
        }
    }
    
    public void notifyReleased(final Widget component, final MouseEvent mouseReleaseEvent) {
        if (this.m_componentPressed == component) {
            MessageScheduler.getInstance().removeAllClocks(this);
            MessageScheduler.getInstance().addClock(this, this.m_doubleClickLatency, component.hashCode(), 1);
            this.simpleClick(component, MouseEvent.checkOut(mouseReleaseEvent));
            return;
        }
        this.m_componentPressed = null;
        this.m_buttonPressed = 0;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (message instanceof ClockMessage) {
            this.m_buttonPressed = 0;
            this.m_componentPressed = null;
            return false;
        }
        return true;
    }
    
    public void doubleClick(final Widget w, final MouseEvent event) {
        event.setType(Events.MOUSE_DOUBLE_CLICKED);
        event.setClickCount(2);
        event.setSoundConsumed(true);
        w.dispatchEvent(event);
    }
    
    public void simpleClick(final Widget w, final MouseEvent event) {
        event.setType(Events.MOUSE_CLICKED);
        event.setClickCount(1);
        w.dispatchEvent(event);
    }
    
    public boolean isMouseOverWidget(final Widget w) {
        final int x = w.getDisplayX();
        final int y = w.getDisplayY();
        return w.getAppearance().insideInsets(this.m_x - x, this.m_y - y);
    }
    
    static {
        m_manager = new MouseManager();
    }
}
