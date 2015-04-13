package com.ankamagames.xulor2.event;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.event.*;

public class ListSelectionChangedEvent extends Event
{
    private RenderableContainer m_renderable;
    private Object m_value;
    private boolean m_selected;
    
    public ListSelectionChangedEvent(final EventDispatcher target, final RenderableContainer renderable, final Object value, final boolean selected) {
        super();
        this.m_renderable = null;
        this.m_value = null;
        this.m_target = target;
        this.m_renderable = renderable;
        this.m_value = value;
        this.m_selected = selected;
    }
    
    public ListSelectionChangedEvent(final EventDispatcher list) {
        super();
        this.m_renderable = null;
        this.m_value = null;
        this.m_target = list;
    }
    
    public void setRenderableContainer(final RenderableContainer renderable) {
        this.m_renderable = renderable;
    }
    
    public RenderableContainer getRenderableContainer() {
        return this.m_renderable;
    }
    
    public void setSelected(final boolean selected) {
        this.m_selected = selected;
    }
    
    public boolean getSelected() {
        return this.m_selected;
    }
    
    public void setValue(final Object value) {
        this.m_value = value;
    }
    
    public Object getValue() {
        return this.m_value;
    }
    
    @Override
    public Events getType() {
        return Events.LIST_SELECTION_CHANGED;
    }
}
