package com.ankamagames.wakfu.common.game.guild.bonus.effect;

public final class SetWeeklyPointsLimit implements GuildBuffEffect
{
    private final int m_newLimit;
    
    public SetWeeklyPointsLimit(final int newLimit) {
        super();
        this.m_newLimit = newLimit;
    }
    
    public int getNewLimit() {
        return this.m_newLimit;
    }
    
    @Override
    public EffectTypeId getType() {
        return EffectTypeId.SET_WEEKLY_POINTS_LIMIT;
    }
}
