package com.ankamagames.baseImpl.graphics.alea.display.effects.strengthHandlers;

import com.ankamagames.framework.kernel.core.maths.*;

public class EaseInStrength extends StrengthHandler
{
    private final int m_startDuration;
    private int m_elapsedTime;
    
    public EaseInStrength(final int startDuration) {
        super(1.0f);
        this.m_startDuration = startDuration;
    }
    
    public EaseInStrength(final int startDuration, final float startStrength) {
        this(startDuration);
        assert startStrength >= 0.0f && startStrength <= 1.0f;
        this.m_elapsedTime = (int)MathHelper.lerp(0.0f, startDuration, startStrength);
    }
    
    @Override
    public boolean update(final int deltaTime) {
        this.m_elapsedTime += deltaTime;
        return super.update(deltaTime);
    }
    
    @Override
    public float getStrength() {
        final float s = MathHelper.clamp(this.m_elapsedTime / this.m_startDuration, 0.0f, 1.0f);
        return super.getStrength() * s;
    }
}
