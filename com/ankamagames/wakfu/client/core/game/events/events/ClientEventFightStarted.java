package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public class ClientEventFightStarted implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final int m_breedId;
    private final long m_groupId;
    
    public ClientEventFightStarted(final int opponentBreed, final long groupId) {
        super();
        this.m_breedId = opponentBreed;
        this.m_groupId = groupId;
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { Integer.toString(this.m_breedId), Long.toString(this.m_groupId) };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventFightStarted.PARAMETERS_LIST_SET;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.FIGHT_STARTED.getId();
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("standard", new Parameter[0]), new ClientGameEventParameterList("filtr\u00e9", new Parameter[] { new Parameter("Breed opposant"), new Parameter("Group Id") }) });
    }
}
