package com.ankamagames.xulor2.event;

import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class DropOutEvent extends MouseEvent
{
    DragNDropContainer m_component;
    Object m_sourceValue;
    Object m_value;
    private static final ObjectPool m_pool;
    
    public DropOutEvent() {
        super();
    }
    
    public DropOutEvent(final DragNDropContainer component, final Object value) {
        super();
        this.setDragNDropable(component);
        this.m_target = component;
        this.m_value = value;
    }
    
    public static DropOutEvent checkOut(final java.awt.event.MouseEvent me, final DragNDropContainer component, final Object value) {
        DropOutEvent e;
        try {
            e = (DropOutEvent)DropOutEvent.m_pool.borrowObject();
            e.m_currentPool = DropOutEvent.m_pool;
        }
        catch (Exception ex) {
            e = new DropOutEvent();
            e.onCheckOut();
        }
        e.setButton(me.getButton());
        e.setClickCount(me.getClickCount());
        e.setModifiers(me.getModifiersEx());
        e.setScreenX(MouseManager.getInstance().getX());
        e.setScreenY(MouseManager.getInstance().getY());
        e.setTarget(component);
        e.setType(Events.DROP_OUT);
        e.setDragNDropable(component);
        e.m_value = value;
        return e;
    }
    
    public static DropOutEvent checkOut(final MouseEvent me, final DragNDropContainer component, final Object value) {
        DropOutEvent e;
        try {
            e = (DropOutEvent)DropOutEvent.m_pool.borrowObject();
            e.m_currentPool = DropOutEvent.m_pool;
        }
        catch (Exception ex) {
            e = new DropOutEvent();
            e.onCheckOut();
        }
        e.setButton(me.m_button);
        e.setClickCount(me.m_clickCount);
        e.setModifiers(me.m_modifiers);
        e.setScreenX(me.m_screenX);
        e.setScreenY(me.m_screenY);
        e.setTarget(component);
        e.setType(Events.DROP_OUT);
        e.setDragNDropable(component);
        e.m_value = value;
        return e;
    }
    
    public DragNDropContainer getDragNDropable() {
        return this.m_component;
    }
    
    public void setDragNDropable(final DragNDropContainer dnd) {
        if (dnd instanceof EventDispatcher) {
            this.m_component = dnd;
        }
        if (dnd != null) {
            final RenderableContainer renderable = dnd.getRenderableParent();
            if (renderable != null) {
                this.m_sourceValue = renderable.getItemValue();
            }
        }
    }
    
    public Object getSourceValue() {
        return this.m_sourceValue;
    }
    
    public void setSourceValue(final Object value) {
        this.m_sourceValue = value;
    }
    
    public Object getValue() {
        return this.m_value;
    }
    
    public void setValue(final Object value) {
        this.m_value = value;
    }
    
    @Override
    public Events getType() {
        return Events.DROP_OUT;
    }
    
    static {
        m_pool = new MonitoredPool(new ObjectFactory<DropOutEvent>() {
            @Override
            public DropOutEvent makeObject() {
                return new DropOutEvent();
            }
        });
    }
}
