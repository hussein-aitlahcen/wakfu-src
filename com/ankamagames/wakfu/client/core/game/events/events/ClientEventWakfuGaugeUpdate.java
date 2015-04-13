package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public class ClientEventWakfuGaugeUpdate implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public String[] getProperties() {
        return new String[0];
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventWakfuGaugeUpdate.PARAMETERS_LIST_SET;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.WAKFU_GAUGE_UPDATED.getId();
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("standard", new Parameter[0]) });
    }
}
