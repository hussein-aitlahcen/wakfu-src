package com.ankamagames.baseImpl.common.clientAndServer.game.effectArea;

import com.ankamagames.framework.external.*;

public class EffectAreaParameterListSet extends ParameterListSet
{
    public EffectAreaParameterListSet(final ParameterList... lists) {
        super(lists);
    }
    
    @Override
    public final boolean mapValueCount(final int count) {
        return this.mapParameterCount(count);
    }
}
