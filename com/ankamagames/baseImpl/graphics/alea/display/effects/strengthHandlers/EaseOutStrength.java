package com.ankamagames.baseImpl.graphics.alea.display.effects.strengthHandlers;

import com.ankamagames.framework.kernel.core.maths.*;

public class EaseOutStrength extends StrengthHandler
{
    private final int m_endDuration;
    private int m_elapsedTime;
    
    public EaseOutStrength(final int endDuration) {
        super(1.0f);
        this.m_endDuration = endDuration;
    }
    
    public EaseOutStrength(final int endDuration, final float startStrength) {
        this(endDuration);
        assert startStrength >= 0.0f && startStrength <= 1.0f;
        this.m_elapsedTime = (int)MathHelper.lerp(this.m_endDuration, 0.0f, startStrength);
    }
    
    @Override
    public boolean update(final int deltaTime) {
        this.m_elapsedTime += deltaTime;
        return this.m_elapsedTime <= this.m_endDuration && super.update(deltaTime);
    }
    
    @Override
    public float getStrength() {
        final float s = MathHelper.clamp((this.m_endDuration - this.m_elapsedTime) / this.m_endDuration, 0.0f, 1.0f);
        return super.getStrength() * s;
    }
}
