package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public class ClientEventCraftLearned implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final int m_craftId;
    
    public ClientEventCraftLearned(final int craftId) {
        super();
        this.m_craftId = craftId;
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { String.valueOf(this.m_craftId) };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventCraftLearned.PARAMETERS_LIST_SET;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.CRAFT_LEARNED.getId();
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("filtr\u00e9", new Parameter[] { new Parameter("Id du m\u00e9tier") }) });
    }
}
