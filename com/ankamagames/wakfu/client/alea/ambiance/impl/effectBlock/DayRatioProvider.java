package com.ankamagames.wakfu.client.alea.ambiance.impl.effectBlock;

import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.models.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;

public class DayRatioProvider extends FloatEffectProvider
{
    private DayRatioProvider(final ProviderModel model) {
        super(model);
    }
    
    public Float getValue() {
        return WakfuGameCalendar.getInstance().getDayPercentage();
    }
    
    public static class Model extends ProviderModel<DayRatioProvider>
    {
        public Model(final int typeId) {
            super(typeId, (ModelVar[])new ModelVar[0]);
        }
        
        @Override
        public DayRatioProvider createInstance() {
            return new DayRatioProvider(this, null);
        }
    }
}
