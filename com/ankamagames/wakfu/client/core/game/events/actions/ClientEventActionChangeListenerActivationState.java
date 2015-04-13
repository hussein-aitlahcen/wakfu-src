package com.ankamagames.wakfu.client.core.game.events.actions;

import com.ankamagames.wakfu.client.core.game.events.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;

final class ClientEventActionChangeListenerActivationState extends ClientEventAction
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_eventId;
    private boolean m_activate;
    
    ClientEventActionChangeListenerActivationState(final int id) {
        super(id);
    }
    
    @Override
    public void execute() {
        final ClientGameEventListener listener = ClientGameEventManager.INSTANCE.getListener(this.m_eventId);
        if (listener == null) {
            ClientEventActionChangeListenerActivationState.m_logger.error((Object)("Evenement d'id inconnnu " + this.m_eventId));
            return;
        }
        listener.setActive(this.m_activate);
        ClientGameEventPreferencesManager.changePreferences(listener);
    }
    
    @Override
    protected void parseParameters(final ArrayList<ParserObject> params) {
        this.m_eventId = 0;
        final int paramCount = params.size();
        if (paramCount != 2) {
            ClientEventActionChangeListenerActivationState.m_logger.error((Object)("Nombre de param\u00e8tres invalide pour une action de type PetText id=" + this.getId() + " paramCount=" + paramCount));
            return;
        }
        this.m_eventId = (int)params.get(0).getLongValue(null, null, null, null);
        this.m_activate = (params.get(1).getValidity(null, null, null, null) == 0);
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventActionChangeListenerActivationState.PARAMETERS_LIST_SET;
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("event \u00e0 modifier", new Parameter[] { new ParserObjectParameter("id", ParserType.NUMBER), new ParserObjectParameter("activ\u00e9/d\u00e9sactiv\u00e9 (true/false)", ParserType.BOOLEAN) }) });
    }
}
