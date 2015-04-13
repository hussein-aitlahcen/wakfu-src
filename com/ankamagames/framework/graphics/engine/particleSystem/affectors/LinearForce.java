package com.ankamagames.framework.graphics.engine.particleSystem.affectors;

import com.ankamagames.framework.graphics.engine.particleSystem.*;

public abstract class LinearForce extends Affector
{
    public final float m_x;
    public final float m_y;
    public final float m_z;
    
    protected LinearForce(final float x, final float y, final float z) {
        super();
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
    }
    
    public static LinearForce create(final float x, final float y, final float z, final boolean applyOnVelocity) {
        return applyOnVelocity ? new Velocity(x, y, z) : new Position(x, y, z);
    }
    
    public static class Position extends LinearForce
    {
        private Position(final float x, final float y, final float z) {
            super(x, y, z);
        }
        
        @Override
        public void affect(final float timeIncrement, final float timeProgressRatio, final Particle reference, final Particle target, final ParticleSystem system) {
            target.m_x += this.m_x * timeIncrement;
            target.m_y += this.m_y * timeIncrement;
            target.m_z += this.m_z * timeIncrement;
        }
    }
    
    public static class Velocity extends LinearForce
    {
        private Velocity(final float x, final float y, final float z) {
            super(x, y, z);
        }
        
        @Override
        public void affect(final float timeIncrement, final float timeProgressRatio, final Particle reference, final Particle target, final ParticleSystem system) {
            target.m_velocityX += this.m_x * timeIncrement;
            target.m_velocityY += this.m_y * timeIncrement;
            target.m_velocityZ += this.m_z * timeIncrement;
        }
    }
}
