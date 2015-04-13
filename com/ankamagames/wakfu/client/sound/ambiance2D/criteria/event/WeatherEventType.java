package com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event;

public enum WeatherEventType
{
    RAIN_BEGIN((byte)0, "D\u00e9but de la pluie", (byte)0), 
    RAIN_END((byte)1, "Fin de la pluie", (byte)0), 
    SNOW_BEGIN((byte)2, "D\u00e9but de la neige", (byte)0), 
    SNOW_END((byte)3, "Fin de la neige", (byte)0), 
    THUNDER((byte)4, "Eclair", (byte)1), 
    CHAOS_START((byte)5, "Chaos-Debut", (byte)2), 
    CHAOS_END((byte)6, "Chaos-Fin", (byte)2);
    
    private String m_description;
    private byte m_id;
    private byte m_subType;
    
    public static WeatherEventType fromId(final byte id) {
        for (final WeatherEventType type : values()) {
            if (type.m_id == id) {
                return type;
            }
        }
        return null;
    }
    
    private WeatherEventType(final byte id, final String description, final byte subType) {
        this.m_description = description;
        this.m_id = id;
        this.m_subType = subType;
    }
    
    public String getDescription() {
        return this.m_description;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public byte getSubType() {
        return this.m_subType;
    }
    
    @Override
    public String toString() {
        return this.m_description;
    }
}
