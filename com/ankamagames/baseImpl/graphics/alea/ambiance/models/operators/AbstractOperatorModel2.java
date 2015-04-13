package com.ankamagames.baseImpl.graphics.alea.ambiance.models.operators;

import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.operations.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;

public abstract class AbstractOperatorModel2<O extends OperatorBlock2> extends AbstractOperatorModel
{
    public static final ModelVar[] TWO_INPUTS;
    
    protected AbstractOperatorModel2(final int typeId) {
        super(typeId, AbstractOperatorModel2.TWO_INPUTS);
    }
    
    static {
        TWO_INPUTS = new ModelVar[] { new ModelVar.MFloat("a"), new ModelVar.MFloat("b") };
    }
}
