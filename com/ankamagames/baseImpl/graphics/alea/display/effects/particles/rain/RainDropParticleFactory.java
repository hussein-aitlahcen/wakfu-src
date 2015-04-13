package com.ankamagames.baseImpl.graphics.alea.display.effects.particles.rain;

import com.ankamagames.baseImpl.graphics.alea.display.effects.*;

class RainDropParticleFactory implements ParticleFactory
{
    @Override
    public Particle newParticle() {
        return new RainDropParticle();
    }
}
