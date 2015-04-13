package com.ankamagames.wakfu.common.game.fight.fightHistory;

public final class SummonFightHistory
{
    private byte m_index;
    private long m_xpAtEndOfFight;
    private short m_levelEarned;
    
    public SummonFightHistory(final byte index, final long xpAtEndOfFight, final short levelEarned) {
        super();
        this.m_index = index;
        this.m_xpAtEndOfFight = xpAtEndOfFight;
        this.m_levelEarned = levelEarned;
    }
    
    public byte getIndex() {
        return this.m_index;
    }
    
    public void setIndex(final byte index) {
        this.m_index = index;
    }
    
    public long getXpAtEndOfFight() {
        return this.m_xpAtEndOfFight;
    }
    
    public void setXpAtEndOfFight(final long xpAtEndOfFight) {
        this.m_xpAtEndOfFight = xpAtEndOfFight;
    }
    
    public short getLevelEarned() {
        return this.m_levelEarned;
    }
    
    public void setLevelEarned(final short levelEarned) {
        this.m_levelEarned = levelEarned;
    }
}
