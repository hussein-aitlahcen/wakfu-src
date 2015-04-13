package com.ankamagames.wakfu.client.core.game.characterInfo.guild;

import com.ankamagames.wakfu.common.game.guild.*;
import com.ankamagames.wakfu.common.game.guild.definition.*;
import org.jetbrains.annotations.*;

public interface ClientGuildInformationHandler extends GuildInformationHandler
{
    String getName();
    
    long getBlazon();
    
    @Nullable
    GuildMember getMember(long p0);
    
    long getBestRank();
    
    int getHavenWorldId();
}
