package com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.operations;

import com.ankamagames.baseImpl.graphics.alea.ambiance.models.operators.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;

public class OperatorBlock1 extends AbstractOperatorBlock<OperatorModel1, Float>
{
    public OperatorBlock1(final OperatorModel1 model) {
        super(model);
    }
    
    @Override
    protected Float getValue() {
        return ((OperatorModel1)this.m_model).m_op.compute(this.m_parameters[0].floatValue());
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
        return null;
    }
}
