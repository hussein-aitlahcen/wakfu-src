package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public final class ClientEventWalkinZoneTrigger implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final long m_ieId;
    
    public ClientEventWalkinZoneTrigger(final long ieId) {
        super();
        this.m_ieId = ieId;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.WALKIN_ZONE_TRIGGER.getId();
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { String.valueOf(this.m_ieId) };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventWalkinZoneTrigger.PARAMETERS_LIST_SET;
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("filtr\u00e9", new Parameter[] { new Parameter("Id de l'IE") }) });
    }
}
