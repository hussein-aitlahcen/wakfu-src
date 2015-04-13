package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public class ClientEventLevelGain implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final int m_newLevel;
    
    public ClientEventLevelGain(final int newLevel) {
        super();
        this.m_newLevel = newLevel;
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { Integer.toString(this.m_newLevel) };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventLevelGain.PARAMETERS_LIST_SET;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.LEVEL_GAIN.getId();
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("standard", new Parameter[0]), new ClientGameEventParameterList("filtr\u00e9", new Parameter[] { new Parameter("level atteind par le joueur") }) });
    }
}
