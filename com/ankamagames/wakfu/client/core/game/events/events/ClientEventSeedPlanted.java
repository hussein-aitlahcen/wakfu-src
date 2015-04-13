package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public class ClientEventSeedPlanted implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final int m_resourceId;
    private final short m_resourceType;
    
    public ClientEventSeedPlanted(final int resourceId, final short resourceType) {
        super();
        this.m_resourceId = resourceId;
        this.m_resourceType = resourceType;
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { String.valueOf(this.m_resourceId), String.valueOf(this.m_resourceType) };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventSeedPlanted.PARAMETERS_LIST_SET;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.SEED_PLANTED.getId();
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("standard", new Parameter[0]), new ClientGameEventParameterList("filtr\u00e9", new Parameter[] { new Parameter("Id de la resource plant\u00e9e"), new Parameter("id de la famille de ressource plant\u00e9e") }) });
    }
}
