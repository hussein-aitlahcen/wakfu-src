package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event;

import com.ankamagames.framework.sound.group.*;

public abstract class SoundEvent
{
    private final EventType m_type;
    private final ObservedSource m_source;
    
    protected SoundEvent(final EventType type) {
        super();
        this.m_type = type;
        this.m_source = null;
    }
    
    public SoundEvent(final EventType type, final ObservedSource source) {
        super();
        this.m_type = type;
        this.m_source = source;
    }
    
    public EventType getType() {
        return this.m_type;
    }
    
    public byte getTypeId() {
        return this.m_type.getId();
    }
    
    public boolean isLocalized() {
        return this.m_source != null;
    }
    
    public ObservedSource getSource() {
        return this.m_source;
    }
    
    public abstract byte getSoundEventType();
    
    public abstract int getSignature();
    
    public abstract String getEventTitle();
    
    public abstract String getParamDescription();
    
    @Override
    public String toString() {
        return this.getEventTitle() + " : " + this.getParamDescription();
    }
}
