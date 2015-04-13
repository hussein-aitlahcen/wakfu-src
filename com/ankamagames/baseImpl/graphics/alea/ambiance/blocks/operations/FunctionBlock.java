package com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.operations;

import com.ankamagames.baseImpl.graphics.alea.ambiance.models.operators.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;

public class FunctionBlock extends AbstractOperatorBlock<FunctionModel, Float>
{
    public FunctionBlock(final FunctionModel model) {
        super(model);
    }
    
    @Override
    protected Float getValue() {
        return ((Var.VSpline)this.m_params[0]).compute(this.m_parameters[0].floatValue());
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
