package com.ankamagames.wakfu.client.core.game.events;

import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;

public class ClientEventTitleUnlocked implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final int m_titleId;
    
    public ClientEventTitleUnlocked(final int titleId) {
        super();
        this.m_titleId = titleId;
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { Integer.toString(this.m_titleId) };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventTitleUnlocked.PARAMETERS_LIST_SET;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.TITLE_UNLOCKED.getId();
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("standard", new Parameter[0]), new ClientGameEventParameterList("filtr\u00e9", new Parameter[] { new Parameter("Id du titre d\u00e9bloqu\u00e9") }) });
    }
}
