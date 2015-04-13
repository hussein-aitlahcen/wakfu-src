package com.ankamagames.baseImpl.graphics.alea.display.effects;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.display.effects.strengthHandlers.*;

public class CameraEffectShake extends CameraEffect
{
    private float m_amplitude;
    private float m_period;
    private int m_elapsedTime;
    
    public CameraEffectShake() {
        super();
        this.m_amplitude = 0.5f;
        this.m_period = 77.0f;
    }
    
    @Override
    public void update(final int timeIncrement) {
        super.update(timeIncrement);
        this.m_elapsedTime += timeIncrement;
        final float effectFactor = this.getStrength() * this.m_amplitude;
        final float angle = this.m_elapsedTime / this.m_period * 1.5707964f;
        final float offsetX = this.applyOnX() ? (Math.abs(MathHelper.cosf(angle)) - 1.0f) : 0.0f;
        final float offsetY = this.applyOnY() ? (-Math.abs(MathHelper.sinf(angle))) : 0.0f;
        this.m_camera.addOffsets(effectFactor * offsetX, effectFactor * offsetY);
    }
    
    public final void start() {
        this.m_elapsedTime = 0;
        final StrengthHandler strengthHandler = new StrengthHandler(1.0f);
        this.start(strengthHandler);
    }
    
    public final void setParams(final float period, final float amplitude) {
        this.m_period = period;
        this.m_amplitude = amplitude;
    }
}
