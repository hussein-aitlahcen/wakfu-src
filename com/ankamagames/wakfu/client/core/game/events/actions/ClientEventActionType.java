package com.ankamagames.wakfu.client.core.game.events.actions;

import com.ankamagames.framework.external.*;

public enum ClientEventActionType implements ExportableEnum, Parameterized
{
    PET_TEXT(1, ClientEventActionPetText.PARAMETERS_LIST_SET), 
    RUN_SCRIPT(2, ClientEventActionRunScript.PARAMETERS_LIST_SET), 
    REQUEST_QUEST_ACTIVATION(3, ClientEventActionRequestQuestActivation.PARAMETERS_LIST_SET), 
    CHANGE_EVENT_ACTIVATION_STATE(4, ClientEventActionChangeListenerActivationState.PARAMETERS_LIST_SET), 
    LAUNCH_TUTORIAL(5, ClientEventActionLaunchTutorial.PARAMETERS_LIST_SET);
    
    private final int m_id;
    private ParameterListSet m_parameters;
    
    private ClientEventActionType(final int id, final ParameterListSet parameters) {
        this.m_id = id;
        this.m_parameters = parameters;
    }
    
    @Override
    public String getEnumId() {
        return Integer.toString(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.toString();
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return this.m_parameters;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public static ClientEventAction createFromType(final int typeId, final int actionId) {
        if (typeId == ClientEventActionType.PET_TEXT.getId()) {
            return new ClientEventActionPetText(actionId);
        }
        if (typeId == ClientEventActionType.RUN_SCRIPT.getId()) {
            return new ClientEventActionRunScript(actionId);
        }
        if (typeId == ClientEventActionType.REQUEST_QUEST_ACTIVATION.getId()) {
            return new ClientEventActionRequestQuestActivation(actionId);
        }
        if (typeId == ClientEventActionType.CHANGE_EVENT_ACTIVATION_STATE.getId()) {
            return new ClientEventActionChangeListenerActivationState(actionId);
        }
        if (typeId == ClientEventActionType.LAUNCH_TUTORIAL.getId()) {
            return new ClientEventActionLaunchTutorial(actionId);
        }
        return null;
    }
}
