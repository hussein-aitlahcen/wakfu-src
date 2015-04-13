package com.ankamagames.wakfu.client.core.game.events.actions;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.achievements.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;

public class ClientEventActionRequestQuestActivation extends ClientEventAction
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_questId;
    
    public ClientEventActionRequestQuestActivation(final int id) {
        super(id);
    }
    
    @Override
    public void execute() {
        final ClientAchievementsContext context = WakfuGameEntity.getInstance().getLocalPlayer().getAchievementsContext();
        if (context.canResetAchievement(this.m_questId)) {
            context.tryToActivate(this.m_questId);
        }
    }
    
    @Override
    protected void parseParameters(final ArrayList<ParserObject> params) {
        final int paramCount = params.size();
        if (paramCount != 1) {
            ClientEventActionRequestQuestActivation.m_logger.error((Object)("Nombre de param\u00e8tres invalides pour une action ClientEventActionRequestQuestActivation id=" + this.getId() + " paramCount=" + paramCount));
            return;
        }
        this.m_questId = (int)params.get(0).getLongValue(null, null, null, null);
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventActionRequestQuestActivation.PARAMETERS_LIST_SET;
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("sp\u00e9cifie l'id de la qu\u00eate \u00e0 activer", new Parameter[] { new ParserObjectParameter("id de la qu\u00eate", ParserType.NUMBER) }) });
    }
}
