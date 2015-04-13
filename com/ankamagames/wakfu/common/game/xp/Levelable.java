package com.ankamagames.wakfu.common.game.xp;

import com.ankamagames.wakfu.common.game.xp.modifications.*;

public interface Levelable
{
    XpTable getXpTable();
    
    short getLevel();
    
    XpModification setLevelAndXp(short p0, long p1);
    
    XpModification setLevel(short p0, boolean p1);
    
    long getXp();
    
    XpModification addXp(long p0);
    
    float getCurrentLevelPercentage();
    
    float getXpGainMultiplier();
}
