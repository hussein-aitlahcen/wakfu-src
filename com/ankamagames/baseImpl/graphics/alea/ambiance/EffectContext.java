package com.ankamagames.baseImpl.graphics.alea.ambiance;

class EffectContext
{
    final Effect m_effect;
    private float m_strength;
    private int m_fromAmbianceId;
    
    EffectContext(final Effect effect, final int ambianceId) {
        super();
        this.m_strength = 1.0f;
        this.m_effect = effect;
        this.m_fromAmbianceId = ambianceId;
    }
    
    public int getFromAmbianceId() {
        return this.m_fromAmbianceId;
    }
    
    public float getStrength() {
        return this.m_strength;
    }
    
    public void setStrength(final float strength) {
        this.m_strength = strength;
    }
    
    public final void update(final int deltaTime) {
        this.m_effect.update(deltaTime);
    }
    
    public final boolean isActive() {
        return this.m_strength > 0.0f;
    }
}
