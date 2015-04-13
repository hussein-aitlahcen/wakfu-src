package com.ankamagames.wakfu.client.core.game.events;

import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;

public class ClientEventPartitionChanged implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final String m_oldCoords;
    private final String m_newCoords;
    
    public ClientEventPartitionChanged(final LocalPartition oldPartition, final LocalPartition newPartition) {
        super();
        final StringBuilder oldCoords = new StringBuilder("{").append(oldPartition.getX()).append(",").append(oldPartition.getY()).append("}");
        final StringBuilder newCoords = new StringBuilder("{").append(newPartition.getX()).append(",").append(newPartition.getY()).append("}");
        this.m_oldCoords = oldCoords.toString();
        this.m_newCoords = newCoords.toString();
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { this.m_oldCoords, this.m_newCoords };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventPartitionChanged.PARAMETERS_LIST_SET;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.PARTITION_CHANGED.getId();
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("standard", new Parameter[0]), new ClientGameEventParameterList("filtr\u00e9", new Parameter[] { new Parameter("Coordonn\u00e9es de partition quitt\u00e9e {x,y}"), new Parameter("Coordonn\u00e9es de partition entr\u00e9e {x,y}") }) });
    }
}
