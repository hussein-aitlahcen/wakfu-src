package com.ankamagames.wakfu.common.game.pvp;

import com.ankamagames.wakfu.common.game.nation.*;

public interface PvpUser
{
    short getLevel();
    
    CitizenComportment getCitizenComportment();
    
    long getGuildId();
    
    int getGuildNationId();
}
