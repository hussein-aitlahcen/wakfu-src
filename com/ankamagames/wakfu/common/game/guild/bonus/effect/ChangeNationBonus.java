package com.ankamagames.wakfu.common.game.guild.bonus.effect;

public final class ChangeNationBonus implements GuildBuffEffect
{
    @Override
    public EffectTypeId getType() {
        return EffectTypeId.CHANGE_NATION;
    }
}
