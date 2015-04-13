package com.ankamagames.baseImpl.graphics.isometric.tween;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.isometric.*;

public class CubicSplineTween extends Tween
{
    private Vector3 m_initialPosition;
    private Vector3 m_initialVelocity;
    private Vector3 m_finalPosition;
    private Vector3 m_finalVelocity;
    private float m_a;
    private float m_b;
    private float m_c;
    private float m_d;
    private float m_e;
    private float m_f;
    private float m_g;
    private float m_h;
    private float m_i;
    private float m_j;
    private float m_k;
    private float m_l;
    private boolean m_vectorsChanged;
    private long m_duration;
    private long m_elapsedTime;
    
    public CubicSplineTween(final IsoWorldTarget target) {
        super(target);
        this.m_initialPosition = new Vector3();
        this.m_initialVelocity = new Vector3();
        this.m_finalPosition = new Vector3();
        this.m_finalVelocity = new Vector3();
        this.m_vectorsChanged = true;
    }
    
    public void setDuration(final long time) {
        this.m_duration = time;
    }
    
    public Vector3 getInitialPosition() {
        return this.m_initialPosition;
    }
    
    public void setInitialPosition(final Vector3 position) {
        this.m_initialPosition = position;
        this.m_vectorsChanged = true;
    }
    
    public Vector3 getInitialVelocity() {
        return this.m_initialVelocity;
    }
    
    public void setInitialVelocity(final Vector3 velocity) {
        this.m_initialVelocity = velocity;
        this.m_vectorsChanged = true;
    }
    
    public Vector3 getFinalPosition() {
        return this.m_finalPosition;
    }
    
    public void setFinalPosition(final Vector3 position) {
        this.m_finalPosition = position;
        this.m_vectorsChanged = true;
    }
    
    public Vector3 getFinalVelocity() {
        return this.m_finalVelocity;
    }
    
    public void setFinalVelocity(final Vector3 velocity) {
        this.m_finalVelocity = velocity;
        this.m_vectorsChanged = true;
    }
    
    private void computeFactors() {
        final float time = 1.0f;
        final float x0 = this.m_initialPosition.getX();
        final float y0 = this.m_initialPosition.getY();
        final float z0 = this.m_initialPosition.getZ();
        final float x = x0 + this.m_initialVelocity.getX() * 1.0f;
        final float y = y0 + this.m_initialVelocity.getY() * 1.0f;
        final float z = z0 + this.m_initialVelocity.getZ() * 1.0f;
        final float x2 = this.m_finalPosition.getX();
        final float y2 = this.m_finalPosition.getY();
        final float z2 = this.m_finalPosition.getZ();
        final float x3 = x2 - this.m_finalVelocity.getX() * 1.0f;
        final float y3 = y2 - this.m_finalVelocity.getY() * 1.0f;
        final float z3 = z2 - this.m_finalVelocity.getZ() * 1.0f;
        this.m_a = x2 - 3.0f * x3 + 3.0f * x - x0;
        this.m_b = 3.0f * x3 - 6.0f * x + 3.0f * x0;
        this.m_c = 3.0f * x - 3.0f * x0;
        this.m_d = x0;
        this.m_e = y2 - 3.0f * y3 + 3.0f * y - y0;
        this.m_f = 3.0f * y3 - 6.0f * y + 3.0f * y0;
        this.m_g = 3.0f * y - 3.0f * y0;
        this.m_h = y0;
        this.m_i = z2 - 3.0f * z3 + 3.0f * z - z0;
        this.m_j = 3.0f * z3 - 6.0f * z + 3.0f * z0;
        this.m_k = 3.0f * z - 3.0f * z0;
        this.m_l = z0;
        this.m_vectorsChanged = false;
    }
    
    public Vector3 getPosition(long time) {
        if (this.m_vectorsChanged) {
            this.computeFactors();
        }
        assert time >= 0L : "Le temps ne doit pas etre inferieur a 0";
        if (time > this.m_duration) {
            time = this.m_duration;
        }
        final float t = time / this.m_duration;
        final float t2 = t * t;
        final float t3 = t2 * t;
        return new Vector3(this.m_a * t3 + this.m_b * t2 + this.m_c * t + this.m_d, this.m_e * t3 + this.m_f * t2 + this.m_g * t + this.m_h, this.m_i * t3 + this.m_j * t2 + this.m_k * t + this.m_l);
    }
    
    public long getDuration() {
        return this.m_duration;
    }
    
    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer(this.getClass().getSimpleName());
        buffer.append(" (duration:").append(this.m_duration).append(") > from=").append(this.m_initialPosition).append(", to=").append(this.m_finalPosition).append(", initVel=").append(this.m_initialVelocity).append(", finalVel=").append(this.m_finalVelocity).append(".");
        return buffer.toString();
    }
    
    @Override
    public float getTweenDuration() {
        return this.m_duration;
    }
    
    @Override
    public void process(final int deltaTime) {
        this.m_elapsedTime += deltaTime;
        if (this.m_elapsedTime > this.m_duration) {
            this.endTween();
        }
        if (this.m_target == null) {
            return;
        }
        final Vector3 position = this.getPosition(this.m_elapsedTime);
        this.m_target.setWorldPosition(position.getX(), position.getY(), position.getZ());
    }
}
