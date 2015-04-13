package com.ankamagames.wakfu.client.core.game.events.actions;

import com.ankamagames.framework.script.*;
import java.util.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;

public class ClientEventActionRunScript extends ClientEventAction
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_scriptId;
    
    public ClientEventActionRunScript(final int id) {
        super(id);
    }
    
    @Override
    public void execute() {
        LuaManager.getInstance().runScript("events/" + Integer.toString(this.m_scriptId) + ".lua");
    }
    
    @Override
    protected void parseParameters(final ArrayList<ParserObject> params) {
        final int paramCount = params.size();
        if (paramCount != 1) {
            ClientEventActionRunScript.m_logger.error((Object)("Nombre de param\u00e8tres invalides pour une action RunScript id=" + this.getId() + " paramCount=" + paramCount));
            return;
        }
        this.m_scriptId = (int)params.get(0).getLongValue(null, null, null, null);
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ClientEventActionRunScript.PARAMETERS_LIST_SET;
    }
    
    static {
        PARAMETERS_LIST_SET = new ClientGameEventParameterListSet(new ParameterList[] { new ClientGameEventParameterList("sp\u00e9cifie l'id du script \u00e0 executer", new Parameter[] { new ParserObjectParameter("id du script", ParserType.NUMBER) }) });
    }
}
