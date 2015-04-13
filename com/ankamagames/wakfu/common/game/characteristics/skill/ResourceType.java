package com.ankamagames.wakfu.common.game.characteristics.skill;

public enum ResourceType
{
    TREE((byte)1, (short)1), 
    MINERAL((byte)2, (short)7, (short)23), 
    PLANT((byte)3, (short)10), 
    CULTIVATION((byte)4, (short)2), 
    MOB((byte)5, (short)16), 
    FISH((byte)6, (short)20, (short)22), 
    TREASURE((byte)7, (short)24);
    
    private final byte m_id;
    private final short m_agtId;
    private final short m_havenWorldCategory;
    
    private ResourceType(final byte id, final short agtId) {
        this.m_id = id;
        this.m_agtId = agtId;
        this.m_havenWorldCategory = -1;
    }
    
    private ResourceType(final byte id, final short agtId, final short havenWorldCategory) {
        this.m_id = id;
        this.m_agtId = agtId;
        this.m_havenWorldCategory = havenWorldCategory;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public short getAgtId() {
        return this.m_agtId;
    }
    
    public static ResourceType getByAgtIdOrHWCategory(final short id) {
        final ResourceType[] types = values();
        for (int i = 0; i < types.length; ++i) {
            final ResourceType type = types[i];
            if (type.m_agtId == id || type.m_havenWorldCategory == id) {
                return type;
            }
        }
        return null;
    }
    
    public static ResourceType getById(final short id) {
        final ResourceType[] types = values();
        for (int i = 0; i < types.length; ++i) {
            final ResourceType type = types[i];
            if (type.m_id == id) {
                return type;
            }
        }
        return null;
    }
    
    public boolean isClimateDepending() {
        switch (this) {
            case TREE:
            case PLANT:
            case CULTIVATION: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
}
