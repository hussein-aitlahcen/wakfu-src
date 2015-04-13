package com.ankamagames.baseImpl.graphics.alea.display.effects;

import com.ankamagames.baseImpl.graphics.alea.display.effects.strengthHandlers.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class CameraEffectSeaSickness extends CameraEffect
{
    private float m_amplitude;
    private float m_rotation;
    private float m_period;
    private int m_elapsedTime;
    
    public CameraEffectSeaSickness() {
        super();
        this.m_amplitude = 5.0f;
        this.m_period = 1500.0f;
        this.m_rotation = 0.025f;
    }
    
    public final void start() {
        this.m_elapsedTime = 0;
        final StrengthHandler strengthHandler = new StrengthHandler(1.0f);
        this.start(strengthHandler);
    }
    
    @Override
    public void update(final int timeIncrement) {
        super.update(timeIncrement);
        this.m_elapsedTime += timeIncrement;
        final float effectFactor = this.m_amplitude * this.getStrength();
        final float angle = this.m_elapsedTime / this.m_period * 1.5707964f;
        final float move = MathHelper.sinf(angle);
        final float offsetX = this.applyOnX() ? move : 0.0f;
        final float offsetY = this.applyOnY() ? move : 0.0f;
        this.m_camera.addOffsets(effectFactor * offsetX, effectFactor * offsetY);
        this.m_camera.addRotationZ(this.getStrength() * this.m_rotation * (MathHelper.sinf(0.7f * angle) - 0.5f));
    }
    
    public void setParams(final float period, final float amplitude, final float rotation) {
        this.m_period = period;
        this.m_amplitude = amplitude;
        this.m_rotation = rotation;
    }
}
