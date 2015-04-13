package com.ankamagames.baseImpl.graphics.alea.adviser;

public class AdviserEvent
{
    private final Adviser m_adviser;
    private final AdviserEventType m_type;
    
    public AdviserEvent(final Adviser adviser, final AdviserEventType type) {
        super();
        this.m_adviser = adviser;
        this.m_type = type;
    }
    
    public Adviser getAdviser() {
        return this.m_adviser;
    }
    
    public AdviserEventType getType() {
        return this.m_type;
    }
    
    public enum AdviserEventType
    {
        ADVISER_ADDED, 
        ADVISER_REMOVED;
    }
}
