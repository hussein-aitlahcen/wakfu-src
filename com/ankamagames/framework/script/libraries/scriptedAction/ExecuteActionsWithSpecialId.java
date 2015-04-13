package com.ankamagames.framework.script.libraries.scriptedAction;

import com.ankamagames.framework.script.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class ExecuteActionsWithSpecialId extends ActionGroupFunction
{
    private static final String NAME = "executeActionsWithSpecialId";
    private static final String DESC = "Execute toutes les actions qui correspondent ? un effet dont l'id (id de l'effet, pas du type de l'effet) est pass? en param?tre";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    ExecuteActionsWithSpecialId(final LuaState luaState, final ActionGroup actionGroup) {
        super(luaState, actionGroup);
    }
    
    @Override
    public String getName() {
        return "executeActionsWithSpecialId";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return ExecuteActionsWithSpecialId.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final int id = this.getParamInt(0);
        for (Action action = this.m_actionGroup.getActionsBySpecialId(id); action != null; action = this.m_actionGroup.getActionsBySpecialId(id)) {
            this.m_actionGroup.runAction(action, false);
        }
    }
    
    @Override
    public String getDescription() {
        return "Execute toutes les actions qui correspondent ? un effet dont l'id (id de l'effet, pas du type de l'effet) est pass? en param?tre";
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("actionId", null, LuaScriptParameterType.INTEGER, false) };
    }
}
