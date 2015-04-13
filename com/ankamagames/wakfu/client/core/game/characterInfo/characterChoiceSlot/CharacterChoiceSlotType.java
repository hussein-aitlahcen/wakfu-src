package com.ankamagames.wakfu.client.core.game.characterInfo.characterChoiceSlot;

import com.ankamagames.framework.kernel.core.maths.*;

public enum CharacterChoiceSlotType
{
    CHARACTER_SLOT(1), 
    UNLOCKABLE_SLOT(2);
    
    private final byte m_id;
    
    private CharacterChoiceSlotType(final int id) {
        this.m_id = MathHelper.ensureByte(id);
    }
    
    public byte getId() {
        return this.m_id;
    }
}
