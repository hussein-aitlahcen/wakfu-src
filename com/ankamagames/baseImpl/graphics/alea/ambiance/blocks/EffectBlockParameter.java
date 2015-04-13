package com.ankamagames.baseImpl.graphics.alea.ambiance.blocks;

import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;

public abstract class EffectBlockParameter<M extends AbstractModel, T> extends AbstractBlock<M>
{
    private T m_precomputedValue;
    
    protected EffectBlockParameter(final M model) {
        super(model);
    }
    
    protected abstract T getValue();
    
    public final T get() {
        if (this.m_precomputedValue == null) {
            this.m_precomputedValue = this.getValue();
        }
        return this.m_precomputedValue;
    }
    
    @Override
    public void update(final int deltaTime) {
        this.m_precomputedValue = null;
    }
    
    public abstract float floatValue();
    
    public abstract int intValue();
    
    public abstract String stringValue();
}
