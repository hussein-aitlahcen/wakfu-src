package com.ankamagames.wakfu.client.alea.ambiance.impl.effectBlock;

import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;

public class WakfuPlayerProvider extends FloatEffectProvider
{
    private WakfuPlayerProvider(final ProviderModel model) {
        super(model);
    }
    
    @Override
    protected Float getValue() {
        WakfuClientInstance.getInstance();
        return WakfuClientInstance.getGameEntity().getLocalPlayer().getWakfuGaugeValue();
    }
    
    public static class Model extends ProviderModel<WakfuPlayerProvider>
    {
        public Model(final int typeId) {
            super(typeId, (ModelVar[])new ModelVar[0]);
        }
        
        @Override
        public WakfuPlayerProvider createInstance() {
            return new WakfuPlayerProvider(this, null);
        }
    }
}
