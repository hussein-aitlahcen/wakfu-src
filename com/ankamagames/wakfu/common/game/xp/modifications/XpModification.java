package com.ankamagames.wakfu.common.game.xp.modifications;

public class XpModification
{
    public static final XpModification NONE;
    private final long m_xpDiff;
    private final short m_levelDiff;
    
    public XpModification(final long xpDiff, final short levelDiff) {
        super();
        this.m_levelDiff = levelDiff;
        this.m_xpDiff = xpDiff;
    }
    
    public short getLevelDifference() {
        return this.m_levelDiff;
    }
    
    public long getXpDifference() {
        return this.m_xpDiff;
    }
    
    public long getXpGain() {
        return Math.max(0L, this.m_xpDiff);
    }
    
    public boolean doesLevelUp() {
        return this.m_levelDiff > 0;
    }
    
    public boolean affectsTarget() {
        return this.m_levelDiff != 0 || this.m_xpDiff != 0L;
    }
    
    static {
        NONE = new XpModification(0L, (short)0);
    }
}
