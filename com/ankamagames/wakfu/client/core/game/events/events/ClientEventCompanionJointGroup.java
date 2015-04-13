package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public final class ClientEventCompanionJointGroup implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.COMPANION_JOIN_GROUP.getId();
    }
    
    @Override
    public String[] getProperties() {
        return ClientEventCompanionJointGroup.NO_PROPERTIES;
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventCompanionJointGroup.PARAMETERS_LIST_SET;
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("standard", new Parameter[0]) });
    }
}
