package com.ankamagames.framework.graphics.engine.particleSystem.affectors;

import gnu.trove.*;
import com.ankamagames.framework.graphics.engine.particleSystem.*;

public class DirectionFollower extends Affector
{
    private static final float AXIAL_X_VECTOR = 1.0f;
    private static final float AXIAL_Y_VECTOR = 0.5f;
    private static final float AXIAL_Z_VECTOR = 0.116279066f;
    private final TObjectFloatHashMap<Particle> m_angle;
    
    public DirectionFollower() {
        super();
        this.m_angle = new TObjectFloatHashMap<Particle>();
    }
    
    @Override
    public void affect(final float timeIncrement, final float timeProgressRatio, final Particle reference, final Particle target, final ParticleSystem system) {
        if (!target.m_geocentric) {
            return;
        }
        float x = target.m_x + reference.getX();
        float y = target.m_y + reference.getY();
        float z = target.m_z + reference.getZ();
        x += system.getX();
        y += system.getY();
        z += system.getZ();
        if (!Float.isNaN(target.m_lastX)) {
            final float dX = x - target.m_lastX;
            final float dY = y - target.m_lastY;
            final float dZ = z - target.m_lastZ;
            if (dX == 0.0f && dY == 0.0f && dZ == 0.0f) {
                return;
            }
            final float rX = (dX - dY) / 4.0f;
            final float rY = (dX + dY) * 0.5f + dZ * 0.116279066f;
            if (Math.abs(rX) > 1.0E-5f) {
                final float angle = (float)Math.atan(rY / rX);
                final float lastAngle = this.m_angle.get(target);
                target.m_angle += angle - lastAngle;
                this.m_angle.put(target, angle);
            }
        }
        target.m_lastX = x;
        target.m_lastY = y;
        target.m_lastZ = z;
    }
}
