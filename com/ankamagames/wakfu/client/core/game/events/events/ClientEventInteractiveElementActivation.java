package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.framework.external.*;

public class ClientEventInteractiveElementActivation implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final long m_elementId;
    private final long m_actionId;
    private final long m_modelId;
    
    public ClientEventInteractiveElementActivation(final WakfuClientMapInteractiveElement ie, final long actionId) {
        super();
        this.m_actionId = actionId;
        this.m_elementId = ie.getId();
        this.m_modelId = ie.getModelId();
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { String.valueOf(this.m_elementId), String.valueOf(this.m_modelId), String.valueOf(this.m_actionId) };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventInteractiveElementActivation.PARAMETERS_LIST_SET;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.INTERACTIVE_ELEMENT_ACTIVATION.getId();
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("standard", new Parameter[0]), new ClientGameEventParameterList("[Element|Template|Action] Id", new Parameter[] { new Parameter("element Id"), new Parameter("model Id"), new Parameter("action Id") }) });
    }
}
