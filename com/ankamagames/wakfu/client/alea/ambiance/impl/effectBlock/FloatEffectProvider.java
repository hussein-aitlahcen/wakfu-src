package com.ankamagames.wakfu.client.alea.ambiance.impl.effectBlock;

import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.providers.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;

public abstract class FloatEffectProvider extends EffectProvider<Float>
{
    protected FloatEffectProvider(final ProviderModel model) {
        super(model);
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
}
