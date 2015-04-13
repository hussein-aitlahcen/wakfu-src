package com.ankamagames.framework.graphics.engine.particleSystem.affectors;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.maths.motion.*;
import com.ankamagames.framework.graphics.engine.particleSystem.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class Curve extends Affector
{
    public static final long DURATION = 1000000L;
    private static final Logger m_logger;
    private final Trajectory m_trajectory;
    
    public Curve(final Trajectory trajectory) {
        super();
        this.m_trajectory = trajectory;
    }
    
    @Override
    public void affect(final float timeIncrement, final float timeProgressRatio, final Particle reference, final Particle target, final ParticleSystem system) {
        final long t = (long)(1000000.0f * (target.m_life / target.m_lifeTime));
        final Vector3 p = this.m_trajectory.getPosition(t);
        if (reference.m_geocentric) {
            target.m_x = p.m_x + system.getX();
            target.m_y = p.m_y + system.getY();
            target.m_z = p.m_z + system.getZ();
        }
        else {
            target.m_x = p.m_x + reference.m_x;
            target.m_y = p.m_y + reference.m_y;
            target.m_z = p.m_z + reference.m_z;
        }
    }
    
    public Vector3 getStartPosition() {
        return this.m_trajectory.getInitialPosition();
    }
    
    public Vector3 getStartVelocity() {
        return this.m_trajectory.getInitialVelocity();
    }
    
    public Vector3 getEndPosition() {
        return this.m_trajectory.getFinalPosition();
    }
    
    public Vector3 getEndVelocity() {
        return this.m_trajectory.getFinalVelocity();
    }
    
    static {
        m_logger = Logger.getLogger((Class)Curve.class);
    }
}
