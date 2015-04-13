package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public class ClientEventNationChanged implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final int m_previousNationId;
    private final int m_newNationId;
    
    public ClientEventNationChanged(final int previousNationId, final int newNationId) {
        super();
        this.m_previousNationId = previousNationId;
        this.m_newNationId = newNationId;
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { String.valueOf(this.m_previousNationId), String.valueOf(this.m_newNationId) };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventNationChanged.PARAMETERS_LIST_SET;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.NATION_CHANGED.getId();
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("standard", new Parameter[0]), new ClientGameEventParameterList("filtr\u00e9", new Parameter[] { new Parameter("previousNationId"), new Parameter("newNationId") }) });
    }
}
