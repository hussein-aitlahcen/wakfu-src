package com.ankamagames.wakfu.client.core.game.wakfu;

public enum EcosystemFamilyType
{
    MONSTER1((byte)1), 
    MONSTER2((byte)2), 
    MONSTER3((byte)3), 
    RESOURCE1((byte)4), 
    RESOURCE2((byte)5), 
    RESOURCE3((byte)6);
    
    private byte m_id;
    private static final int RESSOURCE_INDEX = 3;
    
    private EcosystemFamilyType(final byte b) {
        this.m_id = b;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public static EcosystemFamilyType getFromId(final byte id) {
        for (final EcosystemFamilyType type : values()) {
            if (type.m_id == id) {
                return type;
            }
        }
        return null;
    }
    
    public static EcosystemFamilyType getMonsterTypeFromIndex(final int index) {
        final EcosystemFamilyType[] types = values();
        if (types.length < index || index >= 3) {
            return null;
        }
        return types[index];
    }
    
    public static EcosystemFamilyType getResourceTypeFromIndex(final int index) {
        final EcosystemFamilyType[] types = values();
        final int modIndex = index + 3;
        if (types.length < modIndex || modIndex < 3) {
            return null;
        }
        return types[modIndex];
    }
    
    public boolean isMonster() {
        return this.ordinal() < 3;
    }
}
