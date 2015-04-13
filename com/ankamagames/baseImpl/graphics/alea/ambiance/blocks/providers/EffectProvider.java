package com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.providers;

import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;

public abstract class EffectProvider<T> extends EffectBlockParameter<ProviderModel, T>
{
    protected EffectProvider(final ProviderModel model) {
        super(model);
    }
    
    @Override
    public final void setInputs(final EffectBlockParameter[] parameters) {
        throw new UnsupportedOperationException("pas d'entr\u00e9e sur les providers");
    }
}
