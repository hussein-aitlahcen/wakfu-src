package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public class ClientEventEnterInstance implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final int m_lastInstanceId;
    private final int m_instanceId;
    
    public ClientEventEnterInstance(final int lastInstanceId, final int instanceId) {
        super();
        this.m_lastInstanceId = lastInstanceId;
        this.m_instanceId = instanceId;
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { Integer.toString(this.m_instanceId), Integer.toString(this.m_lastInstanceId) };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventEnterInstance.PARAMETERS_LIST_SET;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.ENTER_INSTANCE.getId();
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("standard", new Parameter[0]), new ClientGameEventParameterList("filtr\u00e9", new Parameter[] { new Parameter("id de l'instance") }), new ClientGameEventParameterList("Ancien monde", new Parameter[] { new Parameter("id de l'instance"), new Parameter("id de l'ancienne instance") }) });
    }
}
