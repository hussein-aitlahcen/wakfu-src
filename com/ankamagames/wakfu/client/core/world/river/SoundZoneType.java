package com.ankamagames.wakfu.client.core.world.river;

import com.ankamagames.framework.sound.group.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event.*;

public enum SoundZoneType
{
    RIVER((byte)0, GeographyEventType.RIVER), 
    LAKE((byte)1, GeographyEventType.LAKE), 
    LAVA((byte)2, GeographyEventType.LAVA), 
    RUNNING_LAVA((byte)3, GeographyEventType.RUNNING_LAVA);
    
    private final byte m_id;
    private ObservedSource m_source;
    private final GeographyEventType m_eventType;
    
    private SoundZoneType(final byte id, final GeographyEventType eventType) {
        this.m_id = id;
        this.m_eventType = eventType;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public ObservedSource getSource() {
        if (this.m_source == null) {
            this.m_source = new SoundZoneObservedSource(this);
        }
        return this.m_source;
    }
    
    public GeographyEventType getEventType() {
        return this.m_eventType;
    }
}
