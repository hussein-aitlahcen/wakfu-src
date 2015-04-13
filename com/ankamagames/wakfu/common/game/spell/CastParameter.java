package com.ankamagames.wakfu.common.game.spell;

public final class CastParameter
{
    private final float m_baseValue;
    private final float m_increment;
    
    public CastParameter(final float baseValue, final float increment) {
        super();
        this.m_baseValue = baseValue;
        this.m_increment = increment;
    }
    
    public float getBaseValue() {
        return this.m_baseValue;
    }
    
    public float getIncrement() {
        return this.m_increment;
    }
    
    public byte getCost(final int spellLevel) {
        return (byte)(this.getBaseValue() + spellLevel * this.getIncrement());
    }
}
