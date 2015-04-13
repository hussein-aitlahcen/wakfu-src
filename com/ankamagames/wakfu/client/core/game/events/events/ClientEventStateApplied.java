package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public final class ClientEventStateApplied implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final int m_stateId;
    
    public ClientEventStateApplied(final int stateId) {
        super();
        this.m_stateId = stateId;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.STATE_APPLIED.getId();
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { Integer.toString(this.m_stateId) };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventStateApplied.PARAMETERS_LIST_SET;
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("standard", new Parameter[0]), new ClientGameEventParameterList("filtr\u00e9", new Parameter[] { new Parameter("id de l'etat") }) });
    }
}
