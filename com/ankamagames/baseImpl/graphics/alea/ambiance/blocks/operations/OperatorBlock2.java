package com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.operations;

import com.ankamagames.baseImpl.graphics.alea.ambiance.models.operators.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;

public class OperatorBlock2 extends AbstractOperatorBlock<OperatorModel2, Float>
{
    public OperatorBlock2(final OperatorModel2 model) {
        super(model);
    }
    
    @Override
    protected Float getValue() {
        return ((OperatorModel2)this.m_model).m_op.compute(this.m_parameters[0].floatValue(), this.m_parameters[1].floatValue());
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
