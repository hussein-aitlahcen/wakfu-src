package com.ankamagames.framework.graphics.engine.particleSystem.affectors;

import com.ankamagames.framework.graphics.engine.particleSystem.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class Rebound extends Affector
{
    public final float m_minX;
    public final float m_minY;
    public final float m_minZ;
    public final float m_maxX;
    public final float m_maxY;
    public final float m_maxZ;
    public final float m_restitutionX;
    public final float m_restitutionY;
    public final float m_restitutionZ;
    public final float m_restitutionRandomX;
    public final float m_restitutionRandomY;
    public final float m_restitutionRandomZ;
    private final boolean hasRandom;
    
    public Rebound(final float minX, final float minY, final float minZ, final float maxX, final float maxY, final float maxZ, final float restitutionX, final float restitutionY, final float restitutionZ, final float restitutionRandomX, final float restitutionRandomY, final float restitutionRandomZ) {
        super();
        this.m_minX = minX;
        this.m_minY = minY;
        this.m_minZ = minZ;
        this.m_maxX = maxX;
        this.m_maxY = maxY;
        this.m_maxZ = maxZ;
        this.m_restitutionX = restitutionX;
        this.m_restitutionY = restitutionY;
        this.m_restitutionZ = restitutionZ;
        this.m_restitutionRandomX = restitutionRandomX;
        this.m_restitutionRandomY = restitutionRandomY;
        this.m_restitutionRandomZ = restitutionRandomZ;
        this.hasRandom = (this.m_restitutionX != 0.0f || this.m_restitutionY != 0.0f || this.m_restitutionRandomZ != 0.0f);
    }
    
    @Override
    public void affect(final float timeIncrement, final float timeProgressRatio, final Particle reference, final Particle target, final ParticleSystem system) {
        final float minX = reference.getX() + this.m_minX;
        final float minY = reference.getY() + this.m_minY;
        final float minZ = reference.getZ() + this.m_minZ;
        final float maxX = reference.getX() + this.m_maxX;
        final float maxY = reference.getY() + this.m_maxY;
        final float maxZ = reference.getZ() + this.m_maxZ;
        boolean collide = false;
        if (target.m_x < minX && target.m_lastX >= minX) {
            collide = true;
            target.m_x = minX;
            target.m_velocityX = -target.m_velocityX;
        }
        if (target.m_x > maxX && target.m_lastX <= maxX) {
            collide = true;
            target.m_x = maxX;
            target.m_velocityX = -target.m_velocityX;
        }
        if (target.m_y < minY && target.m_lastY >= minY) {
            collide = true;
            target.m_y = minY;
            target.m_velocityY = -target.m_velocityY;
        }
        if (target.m_y > maxY && target.m_lastY <= maxY) {
            collide = true;
            target.m_y = maxY;
            target.m_velocityY = -target.m_velocityY;
        }
        if (target.m_z < minZ && target.m_lastZ >= minZ) {
            collide = true;
            target.m_z = minZ;
            target.m_velocityZ = -target.m_velocityZ;
        }
        if (target.m_z > maxZ && target.m_lastZ <= maxZ) {
            collide = true;
            target.m_z = maxZ;
            target.m_velocityZ = -target.m_velocityZ;
        }
        if (collide) {
            if (this.hasRandom) {
                target.m_velocityX *= this.m_restitutionX + MathHelper.randomFloat() * this.m_restitutionRandomX;
                target.m_velocityY *= this.m_restitutionY + MathHelper.randomFloat() * this.m_restitutionRandomY;
                target.m_velocityZ *= this.m_restitutionZ + MathHelper.randomFloat() * this.m_restitutionRandomZ;
            }
            else {
                target.m_velocityX *= this.m_restitutionX;
                target.m_velocityY *= this.m_restitutionY;
                target.m_velocityZ *= this.m_restitutionZ;
            }
        }
        target.m_lastX = target.m_x;
        target.m_lastY = target.m_y;
        target.m_lastZ = target.m_z;
    }
}
