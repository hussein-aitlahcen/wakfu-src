package com.ankamagames.wakfu.common.game.nation.Laws;

import com.ankamagames.framework.external.*;

public class NationLawListSet extends ParameterListSet
{
    public NationLawListSet(final ParameterList... lists) {
        super(lists);
    }
    
    @Override
    public final boolean mapValueCount(final int count) {
        return this.mapParameterCount(count);
    }
}
