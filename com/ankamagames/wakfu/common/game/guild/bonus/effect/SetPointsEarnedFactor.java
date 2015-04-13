package com.ankamagames.wakfu.common.game.guild.bonus.effect;

public final class SetPointsEarnedFactor implements GuildBuffEffect
{
    private final float m_factor;
    
    public SetPointsEarnedFactor(final float factor) {
        super();
        this.m_factor = factor;
    }
    
    @Override
    public EffectTypeId getType() {
        return EffectTypeId.SET_POINTS_EARNED_FACTOR;
    }
    
    public float getFactor() {
        return this.m_factor;
    }
}
