package com.ankamagames.wakfu.common.game.effect;

import com.ankamagames.framework.external.*;

public class WakfuRunningEffectParameterListSet extends ParameterListSet
{
    public WakfuRunningEffectParameterListSet(final ParameterList... lists) {
        super(lists);
    }
    
    @Override
    public final boolean mapValueCount(final int count) {
        if (count > 0) {
            return count % 2 == 0 && this.mapParameterCount(count / 2);
        }
        return this.mapParameterCount(count);
    }
}
