package com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.providers;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;

public class RandomFloatProvider extends EffectProvider<Float>
{
    private RandomFloatProvider(final ProviderModel model) {
        super(model);
    }
    
    public final Float getValue() {
        return MathHelper.random(this.m_inputs[0].floatValue(), this.m_inputs[1].floatValue());
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
    
    public static class Model extends ProviderModel<RandomFloatProvider>
    {
        public Model(final int typeId) {
            super(typeId, (ModelVar[])new ModelVar[] { new ModelVar.MFloat("min"), new ModelVar.MFloat("max") });
        }
        
        @Override
        public final RandomFloatProvider createInstance() {
            return new RandomFloatProvider(this, null);
        }
    }
}
