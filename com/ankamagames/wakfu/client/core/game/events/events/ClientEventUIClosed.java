package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public class ClientEventUIClosed implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final String m_dialogId;
    
    public ClientEventUIClosed(final String dialogId) {
        super();
        this.m_dialogId = dialogId;
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { this.m_dialogId };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventUIClosed.PARAMETERS_LIST_SET;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.UI_CLOSED.getId();
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("filtr\u00e9", new Parameter[] { new Parameter("nom du dialog") }) });
    }
}
