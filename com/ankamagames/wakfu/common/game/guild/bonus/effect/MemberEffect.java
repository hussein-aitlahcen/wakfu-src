package com.ankamagames.wakfu.common.game.guild.bonus.effect;

public final class MemberEffect implements GuildBuffEffect
{
    private final int m_effectId;
    
    public MemberEffect(final int effectId) {
        super();
        this.m_effectId = effectId;
    }
    
    public int getEffectId() {
        return this.m_effectId;
    }
    
    @Override
    public EffectTypeId getType() {
        return EffectTypeId.MEMBER_EFFECT;
    }
}
