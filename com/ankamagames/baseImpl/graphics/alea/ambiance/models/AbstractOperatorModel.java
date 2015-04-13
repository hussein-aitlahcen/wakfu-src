package com.ankamagames.baseImpl.graphics.alea.ambiance.models;

import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.operations.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;

public abstract class AbstractOperatorModel<T extends AbstractOperatorBlock> extends AbstractModel<T>
{
    protected AbstractOperatorModel(final int typeId, final ModelVar[] inputs) {
        this(typeId, inputs, AbstractOperatorModel.EMPTY_VARS);
    }
    
    protected AbstractOperatorModel(final int typeId, final ModelVar[] inputs, final ModelVar[] params) {
        super(typeId, inputs, params);
    }
    
    @Override
    public final BlockType getBlockType() {
        return BlockType.Operator;
    }
}
