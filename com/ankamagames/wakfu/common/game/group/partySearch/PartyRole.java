package com.ankamagames.wakfu.common.game.group.partySearch;

import com.ankamagames.framework.kernel.core.maths.*;

public enum PartyRole
{
    NONE(0), 
    DAMAGE_DEALER(1), 
    TANK(2), 
    HEALER(3), 
    POSITIONER(4), 
    SUPPORT(5);
    
    private final byte m_id;
    
    private PartyRole(final int id) {
        this.m_id = MathHelper.ensureByte(id);
    }
    
    public static PartyRole getFromId(final byte id) {
        for (final PartyRole v : values()) {
            if (id == v.m_id) {
                return v;
            }
        }
        return null;
    }
    
    public byte getId() {
        return this.m_id;
    }
}
