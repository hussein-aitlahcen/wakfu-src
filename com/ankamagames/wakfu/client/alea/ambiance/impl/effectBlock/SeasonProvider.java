package com.ankamagames.wakfu.client.alea.ambiance.impl.effectBlock;

import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;

public class SeasonProvider extends FloatEffectProvider
{
    private SeasonProvider(final ProviderModel model) {
        super(model);
    }
    
    public Float getValue() {
        return (float)WakfuGameCalendar.getInstance().getSeason().ordinal();
    }
    
    public static class Model extends ProviderModel<SeasonProvider>
    {
        public Model(final int typeId) {
            super(typeId, (ModelVar[])new ModelVar[0]);
        }
        
        @Override
        public SeasonProvider createInstance() {
            return new SeasonProvider(this, null);
        }
    }
}
