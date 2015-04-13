package com.ankamagames.framework.graphics.opengl.events;

public class GLRendererEvent
{
    protected final EventType m_type;
    
    public GLRendererEvent(final EventType type) {
        super();
        this.m_type = type;
    }
    
    public EventType getType() {
        return this.m_type;
    }
    
    @Override
    public String toString() {
        return '{' + this.getClass().getSimpleName() + " type : " + this.m_type.toString() + '}';
    }
    
    public enum EventType
    {
        OPENGL_VERSION_TOO_OLD;
    }
}
