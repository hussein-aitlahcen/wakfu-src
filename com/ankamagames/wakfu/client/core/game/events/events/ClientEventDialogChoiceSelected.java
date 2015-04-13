package com.ankamagames.wakfu.client.core.game.events.events;

import com.ankamagames.framework.external.*;

public class ClientEventDialogChoiceSelected implements ClientGameEvent
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private final int m_dialogId;
    private final int m_choiceId;
    
    public ClientEventDialogChoiceSelected(final int dialogId, final int choiceId) {
        super();
        this.m_dialogId = dialogId;
        this.m_choiceId = choiceId;
    }
    
    @Override
    public String[] getProperties() {
        return new String[] { Integer.toString(this.m_dialogId), Integer.toString(this.m_choiceId) };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventDialogChoiceSelected.PARAMETERS_LIST_SET;
    }
    
    @Override
    public int getId() {
        return ClientGameEventProtocol.DIALOG_CHOICE_SELECTED.getId();
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("standard", new Parameter[0]), new ClientGameEventParameterList("filtr\u00e9", new Parameter[] { new Parameter("Id du dialogue"), new Parameter("Id du choix") }) });
    }
}
