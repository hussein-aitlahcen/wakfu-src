package com.ankamagames.wakfu.common.game.xp;

public class NulllXpTable implements XpTable
{
    private static final XpTable m_instance;
    
    public static XpTable getInstance() {
        return NulllXpTable.m_instance;
    }
    
    @Override
    public long getXpByLevel(final int level) {
        return 0L;
    }
    
    @Override
    public short getMaxLevel() {
        return 0;
    }
    
    @Override
    public short getMinLevel() {
        return 0;
    }
    
    @Override
    public long getMinXp() {
        return 0L;
    }
    
    @Override
    public long getMaxXp() {
        return 0L;
    }
    
    @Override
    public boolean isLevelValid(final short level) {
        return level == 0;
    }
    
    @Override
    public boolean isXpValid(final long xp) {
        return xp == 0L;
    }
    
    @Override
    public float getPercentageInLevel(final short level, final long xp) {
        return 0.0f;
    }
    
    @Override
    public long getXpInLevel(final long xp) {
        return 0L;
    }
    
    @Override
    public long getLevelExtent(final short level) {
        return 0L;
    }
    
    @Override
    public short getLevelByXp(final long xp) {
        return 0;
    }
    
    static {
        m_instance = new NulllXpTable();
    }
}
