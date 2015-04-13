package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public class ClientGameEventParameterListSet extends ParameterListSet
{
    public ClientGameEventParameterListSet(final ParameterList... lists) {
        super(lists);
    }
    
    @Override
    public final boolean mapValueCount(final int count) {
        return this.mapParameterCount(count);
    }
}
