package com.ankamagames.baseImpl.graphics.alea.display.effects.strengthHandlers;

public class StrengthHandler
{
    private float m_strength;
    
    public StrengthHandler(final float strength) {
        super();
        this.m_strength = strength;
    }
    
    public float getStrength() {
        return this.m_strength;
    }
    
    public void setStrength(final float strength) {
        this.m_strength = strength;
    }
    
    public boolean update(final int deltaTime) {
        return true;
    }
}
