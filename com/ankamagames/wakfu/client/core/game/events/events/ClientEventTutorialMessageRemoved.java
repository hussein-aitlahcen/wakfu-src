package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public class ClientEventTutorialMessageRemoved implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final int m_eventActionId;
    
    public ClientEventTutorialMessageRemoved(final int eventActionId) {
        super();
        this.m_eventActionId = eventActionId;
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { Integer.toString(this.m_eventActionId) };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventTutorialMessageRemoved.PARAMETERS_LIST_SET;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.TUTORIAL_MESSAGE_REMOVED.getId();
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("standard", new Parameter[0]), new ClientGameEventParameterList("filtr\u00e9", new Parameter[] { new Parameter("id unique de l'action d?\u00e9v\u00e9nement client LAUCH_TUTORIAL") }) });
    }
}
