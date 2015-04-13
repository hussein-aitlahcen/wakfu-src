package com.ankamagames.wakfu.common.game.group.partySearch;

import com.ankamagames.framework.kernel.core.maths.*;

public enum PartyOccupationType
{
    MONSTER(0), 
    DUNGEON(1);
    
    private final byte m_id;
    
    private PartyOccupationType(final int id) {
        this.m_id = MathHelper.ensureByte(id);
    }
    
    public byte getId() {
        return this.m_id;
    }
}
