package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public final class ClientEventGroupKilled implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final long m_groupId;
    
    public ClientEventGroupKilled(final long groupId) {
        super();
        this.m_groupId = groupId;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.GROUP_KILLED_EVENT.getId();
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { Long.toString(this.m_groupId) };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventGroupKilled.PARAMETERS_LIST_SET;
    }
    
    @Override
    public String toString() {
        return "ClientEventGroupKilled{m_groupId=" + this.m_groupId + '}';
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("standard", new Parameter[0]), new ClientGameEventParameterList("filtr\u00e9", new Parameter[] { new Parameter("id du groupe") }) });
    }
}
