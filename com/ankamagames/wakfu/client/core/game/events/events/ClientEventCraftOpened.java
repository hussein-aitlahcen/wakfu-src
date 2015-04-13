package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public class ClientEventCraftOpened implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final int m_craftId;
    
    public ClientEventCraftOpened(final int craftId) {
        super();
        this.m_craftId = craftId;
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { String.valueOf(this.m_craftId) };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventCraftOpened.PARAMETERS_LIST_SET;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.CRAFT_OPENED.getId();
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("filtr\u00e9", new Parameter[] { new Parameter("Cat\u00e9gorie de recette") }) });
    }
}
