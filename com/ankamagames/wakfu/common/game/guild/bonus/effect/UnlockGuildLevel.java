package com.ankamagames.wakfu.common.game.guild.bonus.effect;

import com.ankamagames.wakfu.common.datas.guild.agt_like.*;

public class UnlockGuildLevel implements GuildBuffEffect
{
    private final GuildLevelDataAGT m_level;
    
    public UnlockGuildLevel(final GuildLevelDataAGT guildLevel) {
        super();
        this.m_level = guildLevel;
    }
    
    public GuildLevelDataAGT getLevel() {
        return this.m_level;
    }
    
    @Override
    public EffectTypeId getType() {
        return EffectTypeId.UNLOCK_LEVEL;
    }
}
