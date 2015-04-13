package com.ankamagames.wakfu.common.game.characteristics.craft;

public enum EcosystemActionType
{
    PLANTATION((byte)1), 
    COLLECT((byte)2);
    
    private final byte m_id;
    
    private EcosystemActionType(final byte id) {
        this.m_id = id;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public static EcosystemActionType getById(final byte id) {
        final EcosystemActionType[] types = values();
        for (int i = 0; i < types.length; ++i) {
            final EcosystemActionType type = types[i];
            if (type.m_id == id) {
                return type;
            }
        }
        return null;
    }
}
