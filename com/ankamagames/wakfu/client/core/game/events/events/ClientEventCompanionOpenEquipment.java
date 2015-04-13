package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public final class ClientEventCompanionOpenEquipment implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.COMPANION_OPEN_EQUIPMENT.getId();
    }
    
    @Override
    public String[] getProperties() {
        return ClientEventCompanionOpenEquipment.NO_PROPERTIES;
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventCompanionOpenEquipment.PARAMETERS_LIST_SET;
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("standard", new Parameter[0]) });
    }
}
