package com.ankamagames.wakfu.client.alea.ambiance.impl.effectBlock;

import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;

public class WakfuZoneProvider extends FloatEffectProvider
{
    private WakfuZoneProvider(final ProviderModel model) {
        super(model);
    }
    
    @Override
    protected Float getValue() {
        return 0.0f;
    }
    
    public static class Model extends ProviderModel<WakfuZoneProvider>
    {
        public Model(final int typeId) {
            super(typeId, (ModelVar[])new ModelVar[0]);
        }
        
        @Override
        public WakfuZoneProvider createInstance() {
            return new WakfuZoneProvider(this, null);
        }
    }
}
