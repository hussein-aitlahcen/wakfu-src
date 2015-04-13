package com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.providers;

import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;

public class EnvelopProvider extends TimedEffectProvider<Float>
{
    private EnvelopProvider(final ProviderModel model) {
        super(model);
    }
    
    public final Float getValue() {
        final float duration = this.getDuration();
        if (this.m_time > duration) {
            this.m_time -= (long)duration;
        }
        return ((Var.VSpline)this.m_inputs[1]).compute(this.m_time);
    }
    
    @Override
    public final float floatValue() {
        return ((EffectBlockParameter<M, Float>)this).get();
    }
    
    @Override
    public final int intValue() {
        return (int)(Object)((EffectBlockParameter<M, Float>)this).get();
    }
    
    @Override
    public final String stringValue() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public float getDuration() {
        return this.m_inputs[0].floatValue();
    }
    
    public static class Model extends ProviderModel<EnvelopProvider>
    {
        public Model(final int typeId) {
            super(typeId, (ModelVar[])new ModelVar[] { new ModelVar.MFloat("duration"), new ModelVar.MSpline("spline") });
        }
        
        @Override
        public final EnvelopProvider createInstance() {
            return new EnvelopProvider(this, null);
        }
    }
}
