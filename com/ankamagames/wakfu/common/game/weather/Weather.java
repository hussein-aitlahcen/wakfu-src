package com.ankamagames.wakfu.common.game.weather;

public enum Weather
{
    SUNNY((byte)1), 
    CLOUDY((byte)3), 
    RAIN((byte)4), 
    SNOW((byte)5), 
    STORM((byte)7), 
    BLIZZARD((byte)8), 
    FOGGY((byte)9), 
    CHAOS((byte)10);
    
    private byte m_id;
    
    private Weather(final byte id) {
        this.m_id = id;
    }
    
    public byte getId() {
        return this.m_id;
    }
}
