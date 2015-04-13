package com.ankamagames.framework.graphics.engine.particleSystem.affectors;

import com.ankamagames.framework.graphics.engine.particleSystem.*;

public class ColorFader extends Affector
{
    public final float m_red;
    public final float m_green;
    public final float m_blue;
    public final float m_alpha;
    public final float m_speed;
    
    public ColorFader(final float red, final float green, final float blue, final float alpha, final float speed) {
        super();
        this.m_red = red;
        this.m_green = green;
        this.m_blue = blue;
        this.m_alpha = alpha;
        this.m_speed = speed;
    }
    
    @Override
    public void affect(final float timeIncrement, final float timeProgressRatio, final Particle reference, final Particle target, final ParticleSystem system) {
        final float localSpeed = this.m_speed * timeIncrement;
        target.m_red -= (target.m_red - this.m_red) * localSpeed;
        target.m_green -= (target.m_green - this.m_green) * localSpeed;
        target.m_blue -= (target.m_blue - this.m_blue) * localSpeed;
        target.m_alpha -= (target.m_alpha - this.m_alpha) * localSpeed;
    }
}
