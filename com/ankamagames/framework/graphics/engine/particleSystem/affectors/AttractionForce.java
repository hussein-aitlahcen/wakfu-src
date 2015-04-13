package com.ankamagames.framework.graphics.engine.particleSystem.affectors;

import com.ankamagames.framework.graphics.engine.particleSystem.*;

public class AttractionForce extends Affector
{
    public final float m_intensity;
    public final AttractorAxis m_axis;
    public final float m_offsetX;
    public final float m_offsetY;
    public final float m_offsetZ;
    
    public AttractionForce(final float intensity, final AttractorAxis axis, final float offsetX, final float offsetY, final float offsetZ) {
        super();
        this.m_intensity = intensity;
        this.m_axis = axis;
        this.m_offsetX = offsetX;
        this.m_offsetY = offsetY;
        this.m_offsetZ = offsetZ;
    }
    
    @Override
    public void affect(final float timeIncrement, final float timeProgressRatio, final Particle reference, final Particle target, final ParticleSystem system) {
        final float deltaIntensity = this.m_intensity * timeIncrement;
        float dx = this.m_offsetX - target.m_x;
        float dy = this.m_offsetY - target.m_y;
        float dz = this.m_offsetZ - target.m_z;
        if (!reference.m_geocentric) {
            dx += reference.getX();
            dy += reference.getY();
            dz += reference.getZ();
        }
        final float l = (float)Math.sqrt(dx * dx + dy * dy + dz * dz);
        dx /= l;
        dy /= l;
        dz /= l;
        switch (this.m_axis) {
            case X: {
                target.m_y += dy * deltaIntensity;
                target.m_z += dz * deltaIntensity;
                break;
            }
            case Y: {
                target.m_x += dx * deltaIntensity;
                target.m_z += dz * deltaIntensity;
                break;
            }
            case Z: {
                target.m_x += dx * deltaIntensity;
                target.m_y += dy * deltaIntensity;
                break;
            }
            case All: {
                target.m_x += dx * deltaIntensity;
                target.m_y += dy * deltaIntensity;
                target.m_z += dz * deltaIntensity;
                break;
            }
            default: {
                assert false : "Unknown attractor axis";
                break;
            }
        }
    }
    
    @Override
    public boolean isKeyFramedAffector() {
        return true;
    }
    
    public enum AttractorAxis
    {
        X, 
        Y, 
        Z, 
        All;
    }
}
