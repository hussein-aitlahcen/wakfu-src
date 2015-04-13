package com.ankamagames.wakfu.common.game.ai.antlrcriteria;

public final class IsOnOwnDial extends IsOnEffectAreaType
{
    public IsOnOwnDial() {
        super("caster", 1, true);
    }
    
    @Override
    public Enum getEnum() {
        return WakfuCriterionIds.IS_ON_OWN_DIAL;
    }
}
