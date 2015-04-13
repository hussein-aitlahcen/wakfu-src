package com.ankamagames.baseImpl.graphics.alea.display.effects.particles.snow;

import com.ankamagames.baseImpl.graphics.alea.display.effects.*;

class SnowParticleFactory implements ParticleFactory
{
    @Override
    public Particle newParticle() {
        return new SnowParticle();
    }
}
