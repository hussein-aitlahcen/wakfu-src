package com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.providers;

import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;

public abstract class TimedEffectProvider<T> extends EffectProvider<T>
{
    protected long m_time;
    
    protected TimedEffectProvider(final ProviderModel model) {
        super(model);
    }
    
    @Override
    public final void update(final int deltaTime) {
        super.update(deltaTime);
        this.m_time += deltaTime;
    }
    
    public abstract float getDuration();
}
