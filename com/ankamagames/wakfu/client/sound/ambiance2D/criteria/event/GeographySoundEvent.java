package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event;

import com.ankamagames.framework.sound.group.*;

public class GeographySoundEvent extends SoundEvent
{
    public static final byte GEOGRAPHY_SOUND_EVENT_TYPE_ID = 2;
    private GeographyEventType m_geographyEventType;
    private int m_strength;
    
    public GeographySoundEvent(final GeographyEventType geographyEventType, final int strength) {
        super(EventType.GEOGRAPHY_EVENT);
        this.m_geographyEventType = geographyEventType;
        this.m_strength = strength;
    }
    
    public GeographySoundEvent(final ObservedSource source, final GeographyEventType geographyEventType, final int strength) {
        super(EventType.GEOGRAPHY_EVENT, source);
        this.m_geographyEventType = geographyEventType;
        this.m_strength = strength;
    }
    
    public GeographyEventType getGeographyEventType() {
        return this.m_geographyEventType;
    }
    
    public int getStrength() {
        return this.m_strength;
    }
    
    @Override
    public byte getSoundEventType() {
        return 2;
    }
    
    @Override
    public int getSignature() {
        return this.m_geographyEventType.m_id << 8 | 0x2;
    }
    
    @Override
    public String getEventTitle() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Geography - ").append(this.m_geographyEventType.name());
        return sb.toString();
    }
    
    @Override
    public String getParamDescription() {
        final StringBuilder sb = new StringBuilder();
        sb.append(" : Force = ").append(this.m_strength);
        final ObservedSource source = this.getSource();
        if (source != null) {
            sb.append(" Position = [").append(source.getObservedX()).append(",").append(source.getObservedY()).append("]");
        }
        return sb.toString();
    }
}
