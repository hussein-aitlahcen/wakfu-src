package com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.providers;

import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;

public class ConstFloatProvider extends EffectProvider<Float>
{
    private ConstFloatProvider(final ProviderModel model) {
        super(model);
    }
    
    public final Float getValue() {
        return this.m_params[0].floatValue();
    }
    
    @Override
    public final float floatValue() {
        return this.getValue();
    }
    
    @Override
    public final int intValue() {
        return (int)(Object)this.getValue();
    }
    
    @Override
    public final String stringValue() {
        throw new UnsupportedOperationException();
    }
    
    public static class Model extends ProviderModel<ConstFloatProvider>
    {
        public static final String VALUE_NAME = "value";
        
        public Model(final int typeId) {
            super(typeId, (ModelVar[])new ModelVar[] { new ModelVar.MFloat("value") });
        }
        
        @Override
        public final ConstFloatProvider createInstance() {
            return new ConstFloatProvider(this, null);
        }
    }
}
