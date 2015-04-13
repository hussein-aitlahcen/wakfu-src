package com.ankamagames.baseImpl.graphics.alea.display.effects.particles.rain;

import com.ankamagames.baseImpl.graphics.alea.display.effects.*;
import com.ankamagames.framework.kernel.core.maths.*;

class RainParticle extends Particle
{
    @Override
    public void update(final float timeIncrement) {
        this.m_velocityX += this.m_accelerationX;
        this.m_velocityY += this.m_accelerationY;
        this.m_velocityZ += this.m_accelerationZ;
        if (this.m_angle != 0.0f) {
            final float angleCos = MathHelper.cosf(this.m_angle);
            final float angleSin = MathHelper.sinf(this.m_angle);
            this.m_x += (this.m_velocityX + angleCos * 0.125f * this.m_velocityZ) * timeIncrement;
            this.m_y += (this.m_velocityY - angleCos * 0.125f * this.m_velocityZ) * timeIncrement;
            this.m_z += angleSin * this.m_velocityZ * timeIncrement;
        }
        else {
            this.m_x += this.m_velocityX * timeIncrement;
            this.m_y += this.m_velocityY * timeIncrement;
            this.m_z += this.m_velocityZ * timeIncrement;
        }
        this.m_life += timeIncrement;
    }
}
