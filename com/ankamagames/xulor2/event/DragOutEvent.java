package com.ankamagames.xulor2.event;

import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class DragOutEvent extends MouseEvent
{
    private DragNDropContainer m_source;
    private Object m_sourceValue;
    private Object m_value;
    private DragNDropContainer m_out;
    private Object m_outValue;
    private static final ObjectPool m_pool;
    
    public DragOutEvent() {
        super();
        this.m_source = null;
        this.m_sourceValue = null;
        this.m_value = null;
        this.m_out = null;
        this.m_outValue = null;
    }
    
    public DragOutEvent(final DragNDropContainer source, final DragNDropContainer out, final Object value) {
        super();
        this.m_source = null;
        this.m_sourceValue = null;
        this.m_value = null;
        this.m_out = null;
        this.m_outValue = null;
        this.setSource(source);
        this.setOut(out);
        this.m_target = source;
        this.m_value = value;
    }
    
    public static DragOutEvent checkOut(final MouseEvent me, final DragNDropContainer source, final DragNDropContainer out, final Object value) {
        DragOutEvent e;
        try {
            e = (DragOutEvent)DragOutEvent.m_pool.borrowObject();
            e.m_currentPool = DragOutEvent.m_pool;
        }
        catch (Exception ex) {
            e = new DragOutEvent();
            e.onCheckOut();
        }
        e.setButton(me.m_button);
        e.setClickCount(me.m_clickCount);
        e.setModifiers(me.m_modifiers);
        e.setScreenX(me.m_screenX);
        e.setScreenY(me.m_screenY);
        e.setTarget(source);
        e.setType(Events.DRAG_OUT);
        e.setSource(source);
        e.setOut(out);
        e.m_value = value;
        return e;
    }
    
    public DragNDropContainer getSource() {
        return this.m_source;
    }
    
    public void setSource(final DragNDropContainer dnd) {
        if (dnd instanceof EventDispatcher) {
            this.m_source = dnd;
        }
        if (dnd != null) {
            final RenderableContainer renderable = dnd.getRenderableParent();
            if (renderable != null) {
                this.m_sourceValue = renderable.getItemValue();
            }
        }
    }
    
    public DragNDropContainer getOut() {
        return this.m_out;
    }
    
    public void setOut(final DragNDropContainer dnd) {
        if (dnd instanceof EventDispatcher) {
            this.m_out = dnd;
        }
        if (dnd != null) {
            final RenderableContainer renderable = dnd.getRenderableParent();
            if (renderable != null) {
                this.m_outValue = renderable.getItemValue();
            }
        }
    }
    
    public Object getSourceValue() {
        return this.m_sourceValue;
    }
    
    public Object getOutValue() {
        return this.m_outValue;
    }
    
    public Object getValue() {
        return this.m_value;
    }
    
    @Override
    public Events getType() {
        return Events.DRAG_OUT;
    }
    
    static {
        m_pool = new MonitoredPool(new ObjectFactory<DragOutEvent>() {
            @Override
            public DragOutEvent makeObject() {
                return new DragOutEvent();
            }
        });
    }
}
