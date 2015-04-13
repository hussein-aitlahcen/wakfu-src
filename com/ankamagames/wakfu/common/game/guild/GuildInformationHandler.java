package com.ankamagames.wakfu.common.game.guild;

import gnu.trove.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;

public interface GuildInformationHandler
{
    long getGuildId();
    
    short getLevel();
    
    TIntHashSet getActiveBonuses();
    
    GuildRank getRank(long p0);
    
    int getNationId();
    
    void clear();
}
