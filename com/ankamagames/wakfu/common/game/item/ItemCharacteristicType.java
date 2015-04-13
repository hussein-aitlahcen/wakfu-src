package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public enum ItemCharacteristicType implements CharacteristicType
{
    DURABILITY(1, 0, Integer.MAX_VALUE, 0, 50, 50);
    
    private byte m_id;
    private int m_lowerBound;
    private int m_upperBound;
    private int m_defaultMin;
    private int m_defaultMax;
    private int m_defaultValue;
    
    private ItemCharacteristicType(final int id, final int lowerBound, final int upperBound, final int defaultMin, final int defaultMax, final int defaultValue) {
        this.m_id = (byte)id;
        this.m_lowerBound = lowerBound;
        this.m_upperBound = upperBound;
        this.m_defaultMin = defaultMin;
        this.m_defaultMax = defaultMax;
        this.m_defaultValue = defaultValue;
    }
    
    @Override
    public byte getId() {
        return this.m_id;
    }
    
    @Override
    public byte getCharacteristicType() {
        return 1;
    }
    
    @Override
    public int getLowerBound() {
        return this.m_lowerBound;
    }
    
    @Override
    public int getUpperBound() {
        return this.m_upperBound;
    }
    
    @Override
    public int getDefaultMin() {
        return this.m_defaultMin;
    }
    
    @Override
    public int getDefaultMax() {
        return this.m_defaultMax;
    }
    
    @Override
    public int getDefaultValue() {
        return this.m_defaultValue;
    }
    
    @Override
    public boolean isExpandable() {
        return true;
    }
    
    @Override
    public boolean isNegative() {
        return false;
    }
    
    @Override
    public int getOutFightMax() {
        return -1;
    }
    
    public static ItemCharacteristicType getCharacteristicTypeFromId(final Byte id) {
        for (final ItemCharacteristicType characteristicType : values()) {
            if (characteristicType.getId() == id) {
                return characteristicType;
            }
        }
        return null;
    }
}
