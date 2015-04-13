package com.ankamagames.wakfu.common.game.xp.character;

import com.ankamagames.wakfu.common.game.xp.*;
import com.ankamagames.wakfu.common.game.xp.modifications.*;

public interface PlayerCharacterLevelable
{
    XpTable getXpTable();
    
    short getLevel();
    
    long getCurrentXp();
    
    float getCurrentLevelPercentage();
    
    XpModification setXp(long p0);
    
    XpModification addXp(long p0);
    
    XpModification setPlayerCharacterLevel(short p0);
}
