package com.ankamagames.wakfu.common.game.fight;

public enum TeamTypes
{
    NO_TEAM(false), 
    INITIATING_TEAM(true), 
    TARGETED_TEAM(true), 
    BOTH(true);
    
    private final boolean m_mustHeal;
    
    private TeamTypes(final boolean mustHeal) {
        this.m_mustHeal = mustHeal;
    }
    
    public boolean isMustHeal() {
        return this.m_mustHeal;
    }
}
