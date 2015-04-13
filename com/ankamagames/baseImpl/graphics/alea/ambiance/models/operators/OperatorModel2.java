package com.ankamagames.baseImpl.graphics.alea.ambiance.models.operators;

import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.operations.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;

public class OperatorModel2 extends AbstractOperatorModel2<OperatorBlock2>
{
    public final Operation2 m_op;
    
    public OperatorModel2(final int typeId, final Operation2 op) {
        super(typeId);
        this.m_op = op;
    }
    
    @Override
    public OperatorBlock2 createInstance() {
        return new OperatorBlock2(this);
    }
}
