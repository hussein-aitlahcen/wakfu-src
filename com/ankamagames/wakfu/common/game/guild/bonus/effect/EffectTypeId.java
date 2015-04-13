package com.ankamagames.wakfu.common.game.guild.bonus.effect;

public enum EffectTypeId
{
    MEMBER_EFFECT((short)1), 
    INCREASE_MAX_AUTHORIZED_EVOLUTION((short)2), 
    REDUCE_LEARNING_DURATION((short)3), 
    CRITERION_BONUS((short)4), 
    STORAGE_COMPARTMENT((short)5), 
    SET_WEEKLY_POINTS_LIMIT((short)6), 
    SET_POINTS_EARNED_FACTOR((short)7), 
    UNLOCK_LEVEL((short)8), 
    CHANGE_NATION((short)9);
    
    private final short m_id;
    
    private EffectTypeId(final short id) {
        this.m_id = id;
    }
    
    public short getId() {
        return this.m_id;
    }
}
