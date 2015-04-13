package com.ankamagames.baseImpl.graphics.alea.ambiance.models;

import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.effects.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;

public abstract class AbstractEffectModel extends AbstractModel<EffectBlock>
{
    public static final String ACTIVATOR_ANCHOR_NAME = "@Activator";
    
    protected AbstractEffectModel(final int typeId, final ModelVar[] inputs) {
        super(typeId, inputs, AbstractEffectModel.EMPTY_VARS);
    }
    
    @Override
    public final BlockType getBlockType() {
        return BlockType.Effect;
    }
    
    public abstract EffectApplyer createApplyer();
    
    @Override
    public EffectBlock createInstance() {
        return new EffectBlock(this);
    }
}
