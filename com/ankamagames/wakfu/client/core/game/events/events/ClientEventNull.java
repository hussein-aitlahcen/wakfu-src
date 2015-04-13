package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public final class ClientEventNull implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    public static final String[] EMPTY_PROPERTIES;
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.NULL_EVENT.getId();
    }
    
    @Override
    public String[] getProperties() {
        return ClientEventNull.EMPTY_PROPERTIES;
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventNull.PARAMETERS_LIST_SET;
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("standard", new Parameter[0]) });
        EMPTY_PROPERTIES = new String[0];
    }
}
