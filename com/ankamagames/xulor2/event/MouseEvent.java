package com.ankamagames.xulor2.event;

import org.apache.log4j.*;
import com.ankamagames.xulor2.component.*;
import java.awt.event.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class MouseEvent extends InputEvent
{
    private static Logger m_logger;
    private static final ObjectPool m_pool;
    protected int m_screenX;
    protected int m_screenY;
    protected int m_button;
    protected int m_clickCount;
    protected int m_rotations;
    
    public int getX(final Widget w) {
        if (w != null) {
            return this.m_screenX - w.getDisplayX();
        }
        return 0;
    }
    
    public int getScreenX() {
        return this.m_screenX;
    }
    
    public void setScreenX(final int screenX) {
        this.m_screenX = screenX;
    }
    
    public int getY(final Widget w) {
        if (w != null) {
            return this.m_screenY - w.getDisplayY();
        }
        return 0;
    }
    
    public int getScreenY() {
        return this.m_screenY;
    }
    
    public void setScreenY(final int screenY) {
        this.m_screenY = screenY;
    }
    
    public int getButton() {
        return this.m_button;
    }
    
    public void setButton(final int button) {
        this.m_button = button;
    }
    
    public int getClickCount() {
        return this.m_clickCount;
    }
    
    public void setClickCount(final int clicks) {
        this.m_clickCount = clicks;
    }
    
    public int getRotations() {
        return this.m_rotations;
    }
    
    public void setRotations(final int rotations) {
        this.m_rotations = rotations;
    }
    
    public static MouseEvent checkOut(final java.awt.event.MouseEvent event) {
        final MouseEvent e = checkOut();
        e.m_button = event.getButton();
        e.m_modifiers = event.getModifiersEx();
        e.m_clickCount = event.getClickCount();
        return e;
    }
    
    public static MouseEvent checkOut() {
        MouseEvent e;
        try {
            e = (MouseEvent)MouseEvent.m_pool.borrowObject();
            e.m_currentPool = MouseEvent.m_pool;
        }
        catch (Exception ex) {
            MouseEvent.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
            e = new MouseEvent();
            e.onCheckOut();
        }
        return e;
    }
    
    public static MouseEvent checkOut(final MouseEvent me) {
        final MouseEvent e = checkOut();
        e.setButton(me.m_button);
        e.setClickCount(me.m_clickCount);
        e.setModifiers(me.m_modifiers);
        e.setScreenX(me.m_screenX);
        e.setScreenY(me.m_screenY);
        e.setTarget(me.m_target);
        return e;
    }
    
    @Override
    public void release() {
        super.release();
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    static {
        MouseEvent.m_logger = Logger.getLogger((Class)MouseEvent.class);
        m_pool = new MonitoredPool(new ObjectFactory<MouseEvent>() {
            @Override
            public MouseEvent makeObject() {
                return new MouseEvent();
            }
        });
    }
}
