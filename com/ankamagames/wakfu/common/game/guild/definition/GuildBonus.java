package com.ankamagames.wakfu.common.game.guild.definition;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public interface GuildBonus
{
    int getBonusId();
    
    GameDateConst getBuyDate();
    
    GameDateConst getActivationDate();
    
    boolean addListener(GuildBonusListener p0);
    
    boolean removeListener(GuildBonusListener p0);
}
