package com.ankamagames.wakfu.common.game.guild.bonus.effect;

public final class CriterionBonus implements GuildBuffEffect
{
    @Override
    public EffectTypeId getType() {
        return EffectTypeId.CRITERION_BONUS;
    }
}
