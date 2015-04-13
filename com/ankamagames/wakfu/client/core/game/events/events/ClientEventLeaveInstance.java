package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public final class ClientEventLeaveInstance implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final int m_instanceId;
    
    public ClientEventLeaveInstance(final int instanceId) {
        super();
        this.m_instanceId = instanceId;
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { Integer.toString(this.m_instanceId) };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventLeaveInstance.PARAMETERS_LIST_SET;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.LEAVE_INSTANCE.getId();
    }
    
    @Override
    public String toString() {
        return "ClientEventLeaveInstance{m_instanceId=" + this.m_instanceId + '}';
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("standard", new Parameter[0]), new ClientGameEventParameterList("filtr\u00e9", new Parameter[] { new Parameter("id de l'instance") }) });
    }
}
