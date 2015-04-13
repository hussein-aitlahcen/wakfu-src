package com.ankamagames.baseImpl.graphics.alea.display.effects.particles.snow;

import com.ankamagames.baseImpl.graphics.alea.display.effects.*;

class SnowParticle extends Particle
{
    protected float m_mass;
    protected boolean m_floorContact;
    protected float m_wind;
    protected float m_zAcc;
    
    @Override
    public void update(final float timeIncrement) {
        if (!this.m_floorContact) {
            this.m_accelerationX = this.m_wind / this.m_mass;
            this.m_accelerationZ = this.m_zAcc;
            this.m_velocityX += this.m_accelerationX * timeIncrement;
            this.m_velocityY += this.m_accelerationY * timeIncrement;
            this.m_velocityZ += this.m_accelerationZ * timeIncrement;
            this.m_x += this.m_velocityX * timeIncrement;
            this.m_y += this.m_velocityY * timeIncrement;
            this.m_z += this.m_velocityZ * timeIncrement;
            if (this.m_alpha < 0.3f) {
                this.m_alpha += 0.005f;
            }
            this.m_accelerationX = 0.0f;
            this.m_accelerationY = 0.0f;
            this.m_accelerationZ = 0.0f;
        }
        else {
            this.m_width *= 1.0025f;
            this.m_height = this.m_width;
            this.m_alpha *= 0.99f;
            if (this.m_alpha < 0.01f) {
                this.m_life = this.m_lifeTime;
            }
        }
        this.m_life += timeIncrement;
    }
}
