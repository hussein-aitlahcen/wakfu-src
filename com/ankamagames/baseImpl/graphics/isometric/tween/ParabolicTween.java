package com.ankamagames.baseImpl.graphics.isometric.tween;

import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class ParabolicTween extends Tween
{
    private final float ISOMETRIC_Z_TO_ALTITUDE_COEF = 4.8f;
    private static final float DEFAULT_G = 9.81f;
    private static final float DEFAULT_TIME_PER_SECOND = 1.0f;
    private float m_timePersecond;
    private final float m_g = 9.81f;
    private final float m_startX;
    private final float m_startY;
    private final float m_startZ;
    private float m_deltaZ;
    private final float m_xVelocity;
    private final float m_yVelocity;
    private final float m_zVelocity;
    private final float m_tweenEstimatedTime;
    private int m_elapsedTime;
    
    public ParabolicTween(final IsoWorldTarget target, final float destX, final float destY, final float destZ, final double angle) {
        this(target, destX, destY, destZ, angle, 1.0f);
    }
    
    public ParabolicTween(final IsoWorldTarget target, final float destX, final float destY, final float destZ, double angle, final float timePerSecond) {
        super(target);
        this.m_timePersecond = 1.0f;
        this.m_timePersecond = timePerSecond;
        this.m_startX = this.m_target.getWorldX();
        this.m_startY = this.m_target.getWorldY();
        this.m_startZ = this.m_target.getAltitude();
        this.m_deltaZ = destZ - this.m_startZ;
        angle = (float)Math.toRadians((angle == 0.0) ? 1.0 : angle);
        final float distance = Vector2.length(destX - this.m_startX, destY - this.m_startY);
        final float initialSpeed = (float)Math.sqrt(9.81f * distance / Math.sin(2.0 * angle));
        double linearAngle = Math.atan((destY - this.m_startY) / (destX - this.m_startX));
        if (destX - this.m_startX < 0.0f) {
            linearAngle += 3.141592653589793;
        }
        this.m_tweenEstimatedTime = 2.0f * initialSpeed * (float)Math.sin(angle) / 9.81f;
        final double xyCoeff = initialSpeed * Math.cos(angle);
        this.m_xVelocity = (float)(Math.cos(linearAngle) * xyCoeff);
        this.m_yVelocity = (float)(Math.sin(linearAngle) * xyCoeff);
        this.m_zVelocity = initialSpeed * (float)Math.sin(angle);
        this.m_deltaZ /= this.m_tweenEstimatedTime;
    }
    
    @Override
    public void process(final int deltaTime) {
        this.m_elapsedTime += deltaTime;
        final float elapsedTime = this.m_elapsedTime * (this.m_timePersecond / 1000.0f);
        if (this.m_target == null || elapsedTime > this.m_tweenEstimatedTime) {
            this.endTween();
            return;
        }
        final float x = this.m_xVelocity * elapsedTime + this.m_startX;
        final float y = this.m_yVelocity * elapsedTime + this.m_startY;
        final float z = -4.905f * elapsedTime * elapsedTime + this.m_zVelocity * elapsedTime;
        final float altitude = 8.6f * z + this.m_startZ + elapsedTime * this.m_deltaZ;
        this.m_target.setWorldPosition(x, y, altitude);
    }
    
    @Override
    public float getTweenDuration() {
        return this.m_tweenEstimatedTime * 1000.0f / this.m_timePersecond;
    }
}
