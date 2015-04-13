package com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.providers;

import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;

public class TickProvider extends TimedEffectProvider<Float>
{
    private TickProvider(final ProviderModel model) {
        super(model);
    }
    
    public final Float getValue() {
        if (this.m_time <= this.m_inputs[0].floatValue()) {
            return 0.0f;
        }
        final float period = this.getDuration();
        if (this.m_time <= period) {
            return 1.0f;
        }
        this.m_time -= (long)period;
        return 0.0f;
    }
    
    public final boolean isActive() {
        return this.floatValue() != 0.0f;
    }
    
    @Override
    public float floatValue() {
        return ((EffectBlockParameter<M, Float>)this).get();
    }
    
    @Override
    public int intValue() {
        return (int)(Object)((EffectBlockParameter<M, Float>)this).get();
    }
    
    @Override
    public String stringValue() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public float getDuration() {
        return this.m_inputs[1].floatValue() + this.m_inputs[0].floatValue();
    }
    
    public static class Model extends ProviderModel<TickProvider>
    {
        public Model(final int typeId) {
            super(typeId, (ModelVar[])new ModelVar[] { new ModelVar.MFloat("dur\u00e9e inactif"), new ModelVar.MFloat("dur\u00e9e actif") });
        }
        
        @Override
        public final TickProvider createInstance() {
            return new TickProvider(this, null);
        }
    }
}
