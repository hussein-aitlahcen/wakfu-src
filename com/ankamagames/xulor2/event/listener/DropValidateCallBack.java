package com.ankamagames.xulor2.event.listener;

import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.*;
import java.util.*;

public class DropValidateCallBack extends CallBack
{
    private DragNDropContainer m_source;
    private DragNDropContainer m_destination;
    private Object m_sourceValue;
    private Object m_destinationValue;
    private Object m_value;
    
    public void setFunc(final String func, final ElementMap elementMap) {
        this.setCallBackFunc(func, elementMap);
    }
    
    public void setDragNDropables(final DragNDropContainer source, final DragNDropContainer destination, final Object value) {
        this.m_value = value;
        this.m_source = source;
        this.m_destination = destination;
        if (this.m_source != null) {
            final RenderableContainer renderable = this.m_source.getRenderableParent();
            if (renderable != null) {
                this.m_sourceValue = renderable.getItemValue();
            }
        }
        if (this.m_destination != null) {
            final RenderableContainer renderable = this.m_destination.getRenderableParent();
            if (renderable != null) {
                this.m_destinationValue = renderable.getItemValue();
            }
        }
    }
    
    @Override
    protected void fillParameters(final String[] parameters, final List<Class<?>> parameterTypes, final List<Object> args) {
        parameterTypes.add(DragNDropContainer.class);
        parameterTypes.add(Object.class);
        parameterTypes.add(DragNDropContainer.class);
        parameterTypes.add(Object.class);
        parameterTypes.add(Object.class);
        args.add(this.m_source);
        args.add(this.m_sourceValue);
        args.add(this.m_destination);
        args.add(this.m_destinationValue);
        args.add(this.m_value);
        super.fillParameters(parameters, parameterTypes, args);
    }
    
    public void copyCallback(final DropValidateCallBack listener) {
        listener.setFunc(this.m_func, this.m_elementMap);
    }
    
    public DropValidateCallBack cloneListener() {
        final DropValidateCallBack cb = new DropValidateCallBack();
        this.copyCallback(cb);
        return cb;
    }
    
    public Object invokeCallBack(final DragNDropContainer source, final DragNDropContainer destination, final Object value) {
        this.m_value = value;
        this.m_source = source;
        this.m_destination = destination;
        if (this.m_source != null) {
            final RenderableContainer renderable = this.m_source.getRenderableParent();
            if (renderable != null) {
                this.m_sourceValue = renderable.getItemValue();
            }
        }
        if (this.m_destination != null) {
            final RenderableContainer renderable = this.m_destination.getRenderableParent();
            if (renderable != null) {
                this.m_destinationValue = renderable.getItemValue();
            }
        }
        return super.invokeCallBack();
    }
}
