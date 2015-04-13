package com.ankamagames.wakfu.common.game.pet.definition;

import com.ankamagames.framework.external.*;

public enum HealthPenaltyType implements ExportableEnum
{
    MAX_MEAL_INTERVAL(1), 
    MIN_MEAL_INTERVAL(2), 
    BAD_FEED(3), 
    PLAYER_DEATH(4);
    
    private final byte m_id;
    
    private HealthPenaltyType(final int id) {
        this.m_id = (byte)id;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public static HealthPenaltyType getById(final byte id) {
        for (final HealthPenaltyType type : values()) {
            if (type.m_id == id) {
                return type;
            }
        }
        return null;
    }
    
    @Override
    public String getEnumId() {
        return Byte.toString(this.getId());
    }
    
    @Override
    public String getEnumLabel() {
        return this.name();
    }
    
    @Override
    public String getEnumComment() {
        return this.name();
    }
}
