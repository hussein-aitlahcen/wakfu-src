package com.ankamagames.wakfu.common.game.fight;

public enum PvpModelType
{
    NOT_PVP(false, false), 
    DUEL(true, true), 
    PVP(true, false);
    
    private final boolean m_isPvp;
    private final boolean m_isFriendly;
    
    private PvpModelType(final boolean isPvp, final boolean isFriendly) {
        this.m_isPvp = isPvp;
        this.m_isFriendly = isFriendly;
    }
    
    public boolean isPvp() {
        return this.m_isPvp;
    }
    
    public boolean isFriendly() {
        return this.m_isFriendly;
    }
}
