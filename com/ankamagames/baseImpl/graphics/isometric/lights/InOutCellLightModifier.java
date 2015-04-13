package com.ankamagames.baseImpl.graphics.isometric.lights;

import com.ankamagames.framework.kernel.core.maths.*;

public abstract class InOutCellLightModifier
{
    private final float[] m_startModifierColor;
    private final float[] m_endModifierColor;
    private final long m_fadeDuration;
    protected final float[] m_currentModifierColor;
    protected boolean m_isFadingIn;
    protected long m_currentFadeDuration;
    
    protected InOutCellLightModifier(final float[] startModifierColor, final float[] endModifierColor, final long fadeDuration) {
        super();
        this.m_startModifierColor = startModifierColor;
        this.m_endModifierColor = endModifierColor;
        this.m_fadeDuration = fadeDuration;
        this.m_isFadingIn = true;
        this.m_currentFadeDuration = 0L;
        this.m_currentModifierColor = new float[3];
        System.arraycopy(this.m_startModifierColor, 0, this.m_currentModifierColor, 0, 3);
    }
    
    public boolean needUpdate() {
        final float[] result = this.m_isFadingIn ? this.m_endModifierColor : this.m_startModifierColor;
        return this.m_currentModifierColor[0] != result[0] || this.m_currentModifierColor[1] != result[1] || this.m_currentModifierColor[2] != result[2];
    }
    
    public void update(final int deltaTime) {
        if (!this.needUpdate()) {
            return;
        }
        this.m_currentFadeDuration += deltaTime;
        final float fadePercentage = this.m_currentFadeDuration / this.m_fadeDuration;
        if (fadePercentage >= 1.0f) {
            if (this.m_isFadingIn) {
                System.arraycopy(this.m_endModifierColor, 0, this.m_currentModifierColor, 0, this.m_endModifierColor.length);
            }
            else {
                System.arraycopy(this.m_startModifierColor, 0, this.m_currentModifierColor, 0, this.m_startModifierColor.length);
            }
            return;
        }
        if (this.m_isFadingIn) {
            for (int i = 0; i < 3; ++i) {
                this.m_currentModifierColor[i] = MathHelper.lerp(this.m_startModifierColor[i], this.m_endModifierColor[i], fadePercentage);
            }
        }
        else {
            for (int i = 0; i < 3; ++i) {
                this.m_currentModifierColor[i] = MathHelper.lerp(this.m_endModifierColor[i], this.m_startModifierColor[i], fadePercentage);
            }
        }
    }
    
    public void fadeIn() {
        this.m_isFadingIn = true;
        this.m_currentFadeDuration = 0L;
    }
    
    public void fadeOut() {
        this.m_isFadingIn = false;
        this.m_currentFadeDuration = 0L;
    }
}
