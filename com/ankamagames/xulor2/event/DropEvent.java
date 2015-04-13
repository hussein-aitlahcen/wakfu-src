package com.ankamagames.xulor2.event;

import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class DropEvent extends MouseEvent
{
    private DragNDropContainer m_destination;
    private DragNDropContainer m_source;
    private Object m_sourceValue;
    private Object m_destinationValue;
    private Object m_value;
    private static final ObjectPool m_pool;
    
    public DropEvent() {
        super();
    }
    
    public DropEvent(final DragNDropContainer destination, final DragNDropContainer source, final Object value) {
        super();
        this.setDropped(source);
        this.setDroppedInto(destination);
        this.m_target = destination;
        this.m_value = value;
    }
    
    public static DropEvent checkOut(final MouseEvent me, final DragNDropContainer destination, final DragNDropContainer source, final Object value) {
        DropEvent e;
        try {
            e = (DropEvent)DropEvent.m_pool.borrowObject();
            e.m_currentPool = DropEvent.m_pool;
        }
        catch (Exception ex) {
            e = new DropEvent();
            e.onCheckOut();
        }
        e.setButton(me.m_button);
        e.setClickCount(me.m_clickCount);
        e.setModifiers(me.m_modifiers);
        e.setScreenX(me.m_screenX);
        e.setScreenY(me.m_screenY);
        e.setTarget(destination);
        e.setType(Events.DROP);
        e.setDropped(source);
        e.setDroppedInto(destination);
        e.m_value = value;
        return e;
    }
    
    public DragNDropContainer getDropped() {
        return this.m_source;
    }
    
    public void setDropped(final DragNDropContainer dropped) {
        if (dropped instanceof EventDispatcher) {
            this.m_source = dropped;
        }
        if (dropped != null) {
            final RenderableContainer renderable = dropped.getRenderableParent();
            if (renderable != null) {
                this.m_sourceValue = renderable.getItemValue();
            }
        }
    }
    
    public DragNDropContainer getDroppedInto() {
        return this.m_destination;
    }
    
    public void setDroppedInto(final DragNDropContainer droppedInto) {
        if (droppedInto instanceof EventDispatcher) {
            this.m_destination = droppedInto;
        }
        if (droppedInto != null) {
            final RenderableContainer renderable = droppedInto.getRenderableParent();
            if (renderable != null) {
                this.m_destinationValue = renderable.getItemValue();
            }
        }
    }
    
    public Object getDestinationValue() {
        return this.m_destinationValue;
    }
    
    public void setDestinationValue(final Object componentValue) {
        this.m_destinationValue = componentValue;
    }
    
    public Object getSourceValue() {
        return this.m_sourceValue;
    }
    
    public void setSourceValue(final Object sourceValue) {
        this.m_sourceValue = sourceValue;
    }
    
    public Object getValue() {
        return this.m_value;
    }
    
    public void setValue(final Object value) {
        this.m_value = value;
    }
    
    @Override
    public Events getType() {
        return Events.DROP;
    }
    
    static {
        m_pool = new MonitoredPool(new ObjectFactory<DropEvent>() {
            @Override
            public DropEvent makeObject() {
                return new DropEvent();
            }
        });
    }
}
