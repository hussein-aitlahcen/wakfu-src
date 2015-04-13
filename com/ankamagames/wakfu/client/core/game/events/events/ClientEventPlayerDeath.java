package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public class ClientEventPlayerDeath implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public String[] getProperties() {
        return new String[0];
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventPlayerDeath.PARAMETERS_LIST_SET;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.PLAYER_DEATH.getId();
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("standard", new Parameter[0]) });
    }
}
