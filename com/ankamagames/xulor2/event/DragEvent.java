package com.ankamagames.xulor2.event;

import org.apache.log4j.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class DragEvent extends MouseEvent
{
    private static Logger m_logger;
    private DragNDropContainer m_component;
    private Object m_sourceValue;
    private Object m_value;
    private static final ObjectPool m_pool;
    
    public DragEvent() {
        super();
        this.m_component = null;
        this.m_sourceValue = null;
        this.m_value = null;
    }
    
    public DragEvent(final DragNDropContainer component, final Object value) {
        super();
        this.m_component = null;
        this.m_sourceValue = null;
        this.m_value = null;
        this.setDragNDropable(component);
        this.m_target = component;
        this.m_value = value;
    }
    
    public static DragEvent checkOut(final MouseEvent me, final EventDispatcher target, final Object value) {
        DragEvent e;
        try {
            e = (DragEvent)DragEvent.m_pool.borrowObject();
            e.m_currentPool = DragEvent.m_pool;
        }
        catch (Exception ex) {
            DragEvent.m_logger.error((Object)"Probl\u00e8me au borrowObject.");
            e = new DragEvent();
            e.onCheckOut();
        }
        e.setButton(me.m_button);
        e.setClickCount(me.m_clickCount);
        e.setModifiers(me.m_modifiers);
        e.setScreenX(me.m_screenX);
        e.setScreenY(me.m_screenY);
        e.setTarget(target);
        e.setType(Events.DRAG);
        e.setDragNDropable((DragNDropContainer)target);
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
        return Events.DRAG;
    }
    
    static {
        DragEvent.m_logger = Logger.getLogger((Class)DragEvent.class);
        m_pool = new MonitoredPool(new ObjectFactory<DragEvent>() {
            @Override
            public DragEvent makeObject() {
                return new DragEvent();
            }
        });
    }
}
