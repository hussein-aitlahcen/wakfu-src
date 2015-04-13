package com.ankamagames.baseImpl.graphics.alea.ambiance.models.operators;

import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.operations.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;

public abstract class AbstractOperatorModel1<O extends OperatorBlock1> extends AbstractOperatorModel<O>
{
    public static final ModelVar[] ONE_INPUT;
    
    protected AbstractOperatorModel1(final int typeId, final ModelVar[] params) {
        super(typeId, AbstractOperatorModel1.ONE_INPUT, params);
    }
    
    protected AbstractOperatorModel1(final int typeId) {
        this(typeId, AbstractOperatorModel1.EMPTY_VARS);
    }
    
    static {
        ONE_INPUT = new ModelVar[] { new ModelVar.MFloat("") };
    }
}
