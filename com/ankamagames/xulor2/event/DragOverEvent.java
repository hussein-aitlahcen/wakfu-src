package com.ankamagames.xulor2.event;

import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class DragOverEvent extends MouseEvent
{
    private DragNDropContainer m_source;
    private Object m_sourceValue;
    private Object m_value;
    private DragNDropContainer m_over;
    private Object m_overValue;
    private static final ObjectPool m_pool;
    
    public DragOverEvent() {
        super();
        this.m_source = null;
        this.m_sourceValue = null;
        this.m_value = null;
        this.m_over = null;
        this.m_overValue = null;
    }
    
    public DragOverEvent(final DragNDropContainer source, final DragNDropContainer over, final Object value) {
        super();
        this.m_source = null;
        this.m_sourceValue = null;
        this.m_value = null;
        this.m_over = null;
        this.m_overValue = null;
        this.setSource(source);
        this.setOver(over);
        this.m_target = source;
        this.m_value = value;
    }
    
    public static DragOverEvent checkOut(final MouseEvent me, final DragNDropContainer source, final DragNDropContainer over, final Object value) {
        DragOverEvent e;
        try {
            e = (DragOverEvent)DragOverEvent.m_pool.borrowObject();
            e.m_currentPool = DragOverEvent.m_pool;
        }
        catch (Exception ex) {
            e = new DragOverEvent();
            e.onCheckOut();
        }
        e.setButton(me.m_button);
        e.setClickCount(me.m_clickCount);
        e.setModifiers(me.m_modifiers);
        e.setScreenX(me.m_screenX);
        e.setScreenY(me.m_screenY);
        e.setTarget(source);
        e.setType(Events.DRAG_OVER);
        e.setSource(source);
        e.setOver(over);
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
    
    public DragNDropContainer getOver() {
        return this.m_over;
    }
    
    public void setOver(final DragNDropContainer dnd) {
        if (dnd instanceof EventDispatcher) {
            this.m_over = dnd;
        }
        if (dnd != null) {
            final RenderableContainer renderable = dnd.getRenderableParent();
            if (renderable != null) {
                this.m_overValue = renderable.getItemValue();
            }
        }
    }
    
    public Object getSourceValue() {
        return this.m_sourceValue;
    }
    
    public Object getOverValue() {
        return this.m_overValue;
    }
    
    @Override
    public Events getType() {
        return Events.DRAG_OVER;
    }
    
    static {
        m_pool = new MonitoredPool(new ObjectFactory<DragOverEvent>() {
            @Override
            public DragOverEvent makeObject() {
                return new DragOverEvent();
            }
        });
    }
}
