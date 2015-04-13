package com.ankamagames.framework.graphics.engine.particleSystem;

public interface AffectorCondition
{
    boolean validate(Particle p0, Particle p1, float p2, ParticleSystem p3);
}
