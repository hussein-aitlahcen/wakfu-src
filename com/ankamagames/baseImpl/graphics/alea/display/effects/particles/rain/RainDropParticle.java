package com.ankamagames.baseImpl.graphics.alea.display.effects.particles.rain;

import com.ankamagames.baseImpl.graphics.alea.display.effects.*;

class RainDropParticle extends Particle
{
    private float m_alphaAdd;
    private float m_widthAdd;
    private float m_heightAdd;
    
    RainDropParticle() {
        super();
        this.m_alphaAdd = -0.025f;
        this.m_widthAdd = 0.5f;
        this.m_heightAdd = 0.33f;
    }
    
    @Override
    public void update(final float timeIncrement) {
        this.m_alpha += this.m_alphaAdd;
        this.m_width += this.m_widthAdd;
        this.m_height += this.m_heightAdd;
        this.m_life += timeIncrement;
    }
}
