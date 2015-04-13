package com.ankamagames.wakfu.common.game.pvp;

import com.ankamagames.framework.kernel.core.maths.*;

public enum MatchType
{
    TOTAL(0), 
    ONE_VS_ONE(1), 
    TWO_VS_TWO(2), 
    THREE_VS_THREE(3), 
    FOUR_VS_FOUR(4), 
    FIVE_VS_FIVE(5), 
    SIX_VS_SIX(6);
    
    private final byte m_id;
    
    private MatchType(final int id) {
        this.m_id = MathHelper.ensureByte(id);
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public static MatchType getFromNumFighters(final int numFighters) {
        final MatchType[] values = values();
        if (numFighters < 0 || numFighters >= values.length) {
            return MatchType.TOTAL;
        }
        return values[numFighters];
    }
}
