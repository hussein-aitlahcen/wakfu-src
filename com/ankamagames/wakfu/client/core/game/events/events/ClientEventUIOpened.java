package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public class ClientEventUIOpened implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final String m_dialogId;
    
    public ClientEventUIOpened(final String dialogId) {
        super();
        this.m_dialogId = dialogId;
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { this.m_dialogId };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventUIOpened.PARAMETERS_LIST_SET;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.UI_OPENED.getId();
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("filtr\u00e9", new Parameter[] { new Parameter("nom du dialog") }) });
    }
}
