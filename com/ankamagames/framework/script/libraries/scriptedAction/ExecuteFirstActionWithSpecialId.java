package com.ankamagames.framework.script.libraries.scriptedAction;

import com.ankamagames.framework.script.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class ExecuteFirstActionWithSpecialId extends ActionGroupFunction
{
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULTS;
    private static final String NAME = "executeFirstActionWithSpecialId";
    private static final String DESC = "Execute la premi?re action qui correspond ? un effet dont l'id (id de l'effet, pas du type de l'effet) est pass? en param?tre";
    
    ExecuteFirstActionWithSpecialId(final LuaState luaState, final ActionGroup actionGroup) {
        super(luaState, actionGroup);
    }
    
    @Override
    public String getName() {
        return "executeFirstActionWithSpecialId";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return ExecuteFirstActionWithSpecialId.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return ExecuteFirstActionWithSpecialId.RESULTS;
    }
    
    public void run(final int paramCount) throws LuaException {
        final int id = this.getParamInt(0);
        final Action action = this.m_actionGroup.getActionsBySpecialId(id);
        if (action != null) {
            this.m_actionGroup.runAction(action, false);
            this.addReturnValue(true);
        }
        else {
            this.addReturnValue(false);
        }
    }
    
    @Override
    public String getDescription() {
        return "Execute la premi?re action qui correspond ? un effet dont l'id (id de l'effet, pas du type de l'effet) est pass? en param?tre";
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("actionId", null, LuaScriptParameterType.INTEGER, false) };
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("actionExists", null, LuaScriptParameterType.BOOLEAN, false) };
    }
}
