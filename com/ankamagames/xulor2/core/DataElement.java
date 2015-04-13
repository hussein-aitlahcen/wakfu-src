package com.ankamagames.xulor2.core;

public abstract class DataElement extends BasicElement
{
    @Override
    public void addFromXML(final EventDispatcher e) {
        this.getParentOfType(EventDispatcher.class).addFromXML(e);
    }
    
    @Override
    public void add(final EventDispatcher e) {
        this.getParentOfType(EventDispatcher.class).add(e);
    }
    
    @Override
    public ElementType getElementType() {
        return ElementType.DATA;
    }
}
