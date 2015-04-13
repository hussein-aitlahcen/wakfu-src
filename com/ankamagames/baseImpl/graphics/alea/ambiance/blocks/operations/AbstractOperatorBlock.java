package com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.operations;

import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;

public abstract class AbstractOperatorBlock<M extends AbstractOperatorModel, T> extends EffectBlockParameter<M, T>
{
    public AbstractOperatorBlock(final M model) {
        super(model);
    }
    
    @Override
    public void update(final int deltaTime) {
    }
}
