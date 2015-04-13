package com.ankamagames.framework.sound.ground;

public enum GroundSoundType
{
    DEFAULT((byte)0), 
    CONCRETE((byte)1), 
    WOOD((byte)2), 
    METAL((byte)3), 
    SAND((byte)4), 
    WATER((byte)5), 
    TEXTILE((byte)6), 
    SNOW((byte)7), 
    WOOD_BRIDGE((byte)8), 
    WOOD_SOFT((byte)9), 
    GRASS((byte)10), 
    EARTH((byte)11), 
    SLIME((byte)12), 
    ICE((byte)13), 
    KAMAS((byte)14);
    
    private final byte m_type;
    
    private GroundSoundType(final byte type) {
        this.m_type = type;
    }
    
    public byte getType() {
        return this.m_type;
    }
}
