package com.ankamagames.baseImpl.graphics.alea.ambiance.models;

import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.providers.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;

public abstract class ProviderModel<T extends EffectProvider> extends AbstractModel<T>
{
    protected ProviderModel(final int typeId, final ModelVar... params) {
        super(typeId, ProviderModel.EMPTY_VARS, params);
    }
    
    @Override
    public final BlockType getBlockType() {
        return BlockType.Provider;
    }
}
