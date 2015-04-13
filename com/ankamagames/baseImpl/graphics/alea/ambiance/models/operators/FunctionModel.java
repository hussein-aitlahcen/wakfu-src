package com.ankamagames.baseImpl.graphics.alea.ambiance.models.operators;

import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.operations.*;

public class FunctionModel extends AbstractOperatorModel1
{
    public FunctionModel(final int typeId) {
        super(typeId, new ModelVar[] { new ModelVar.MSpline("spline") });
    }
    
    @Override
    public AbstractBlock createInstance() {
        return new FunctionBlock(this);
    }
}
