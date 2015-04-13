package com.ankamagames.wakfu.common.game.xp;

public interface XpTable
{
    long getXpByLevel(int p0);
    
    short getLevelByXp(long p0);
    
    short getMaxLevel();
    
    short getMinLevel();
    
    long getMinXp();
    
    long getMaxXp();
    
    boolean isLevelValid(short p0);
    
    boolean isXpValid(long p0);
    
    float getPercentageInLevel(short p0, long p1);
    
    long getXpInLevel(long p0);
    
    long getLevelExtent(short p0);
}
