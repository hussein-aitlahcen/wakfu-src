package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public class ClientEventTerritoryEntrance implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final int m_territoryId;
    
    public ClientEventTerritoryEntrance(final int territoryId) {
        super();
        this.m_territoryId = territoryId;
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { Integer.toString(this.m_territoryId) };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventTerritoryEntrance.PARAMETERS_LIST_SET;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.TERRITORY_ENTRANCE.getId();
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("standard", new Parameter[0]), new ClientGameEventParameterList("standard", new Parameter[] { new Parameter("Id du territoire") }) });
    }
}
