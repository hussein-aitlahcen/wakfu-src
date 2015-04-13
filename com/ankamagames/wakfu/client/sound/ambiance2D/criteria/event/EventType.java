package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event;

public enum EventType
{
    WEATHER_EVENT((byte)0, "Ev\u00e8nement m\u00e9t\u00e9o"), 
    FAUNA_EVENT((byte)1, "Ev\u00e8nement de faune"), 
    FLORA_EVENT((byte)2, "Ev\u00e8nement de flore"), 
    GEOGRAPHY_EVENT((byte)3, "Ev\u00e8nement g\u00e9ographique"), 
    AUDIO_MARKER_EVENT((byte)4, "Ev\u00e8nement de marqueur audio"), 
    TIME_EVENT((byte)5, "Ev\u00e8nement de Temps");
    
    private final String m_description;
    private final byte m_id;
    
    private EventType(final byte id, final String description) {
        this.m_description = description;
        this.m_id = id;
    }
    
    public String getDescription() {
        return this.m_description;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    @Override
    public String toString() {
        return this.m_description;
    }
    
    public static EventType fromId(final byte id) {
        for (final EventType type : values()) {
            if (type.m_id == id) {
                return type;
            }
        }
        return null;
    }
}
