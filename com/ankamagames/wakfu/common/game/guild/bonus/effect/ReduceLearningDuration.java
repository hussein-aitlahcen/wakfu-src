package com.ankamagames.wakfu.common.game.guild.bonus.effect;

public final class ReduceLearningDuration implements GuildBuffEffect
{
    @Override
    public EffectTypeId getType() {
        return EffectTypeId.REDUCE_LEARNING_DURATION;
    }
}
