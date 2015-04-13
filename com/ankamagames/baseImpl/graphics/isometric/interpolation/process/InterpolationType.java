package com.ankamagames.baseImpl.graphics.isometric.interpolation.process;

import com.ankamagames.framework.kernel.core.maths.*;

public enum InterpolationType implements InterpolationProcess
{
    SINUS((InterpolationProcess)new InterpolationProcess() {
        @Override
        public float invoke(final float start, final float value, final float end, final float amount) {
            return value + (end - value) * MathHelper.sin(amount * 1.5707964f);
        }
    }), 
    SMOOTH_LINEAR((InterpolationProcess)new InterpolationProcess() {
        @Override
        public float invoke(final float start, final float value, final float end, final float amount) {
            return MathHelper.lerp(value, end, amount);
        }
    }), 
    HARD_LINEAR((InterpolationProcess)new InterpolationProcess() {
        @Override
        public float invoke(final float start, final float value, final float end, final float amount) {
            final float min = Math.min(start, end);
            final float max = Math.max(start, end);
            return MathHelper.clamp(value + amount * (end - start), min, max);
        }
    }), 
    DIRECT((InterpolationProcess)new InterpolationProcess() {
        @Override
        public float invoke(final float start, final float value, final float end, final float amount) {
            return end;
        }
    });
    
    private final InterpolationProcess m_process;
    
    private InterpolationType(final InterpolationProcess process) {
        this.m_process = process;
    }
    
    @Override
    public float invoke(final float start, final float value, final float end, final float amount) {
        return this.m_process.invoke(start, value, end, amount);
    }
}
