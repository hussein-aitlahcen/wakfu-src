package com.ankamagames.wakfu.common.game.guild.definition;

import gnu.trove.*;
import com.ankamagames.wakfu.common.game.guild.*;

public interface GuildRank
{
    long getId();
    
    String getName();
    
    long getAuthorisations();
    
    void setPosition(short p0);
    
    short getPosition();
    
    boolean forEachAuthorisation(TObjectProcedure<GuildRankAuthorisation> p0);
    
    boolean hasAuthorisation(GuildRankAuthorisation p0);
    
    boolean hasAuthorisation(GuildRankAuthorisation p0, short p1);
    
    boolean addListener(GuildRankListener p0);
    
    boolean removeListener(GuildRankListener p0);
}
