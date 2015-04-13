package com.ankamagames.framework.ai.targetfinder.aoe;

import com.ankamagames.framework.external.*;

public class AOEParametersListSet extends ParameterListSet
{
    public AOEParametersListSet(final ParameterList... lists) {
        super(lists);
    }
    
    @Override
    public final boolean mapValueCount(final int count) {
        return this.mapParameterCount(count);
    }
}
