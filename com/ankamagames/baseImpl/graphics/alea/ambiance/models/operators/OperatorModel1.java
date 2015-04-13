package com.ankamagames.baseImpl.graphics.alea.ambiance.models.operators;

import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.operations.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;

public class OperatorModel1 extends AbstractOperatorModel1<OperatorBlock1>
{
    public final Operation1 m_op;
    
    public OperatorModel1(final int typeId, final Operation1 op) {
        super(typeId);
        this.m_op = op;
    }
    
    @Override
    public OperatorBlock1 createInstance() {
        return new OperatorBlock1(this);
    }
}
