package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public final class ClientEventGuardJobLearned implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.GUARD_JOB_LEARNED.getId();
    }
    
    @Override
    public String[] getProperties() {
        return ClientEventGuardJobLearned.NO_PROPERTIES;
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventGuardJobLearned.PARAMETERS_LIST_SET;
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("standard", new Parameter[0]) });
    }
}
