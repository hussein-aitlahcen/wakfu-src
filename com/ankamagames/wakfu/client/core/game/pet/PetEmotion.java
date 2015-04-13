package com.ankamagames.wakfu.client.core.game.pet;

import com.ankamagames.framework.external.*;

public enum PetEmotion implements ExportableEnum
{
    NEUTRAL(0), 
    JOY(1), 
    ANGER(2), 
    FEAR(3), 
    AGRESSIVITY(4);
    
    private final int m_id;
    
    private PetEmotion(final int id) {
        this.m_id = id;
    }
    
    public static PetEmotion getFromId(final int id) {
        for (final PetEmotion e : values()) {
            if (e.m_id == id) {
                return e;
            }
        }
        return PetEmotion.NEUTRAL;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.toString();
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
}
