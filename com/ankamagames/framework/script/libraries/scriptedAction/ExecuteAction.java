package com.ankamagames.framework.script.libraries.scriptedAction;

import com.ankamagames.framework.script.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class ExecuteAction extends ActionGroupFunction
{
    private static final String NAME = "executeAction";
    private static final String DESC = "Force l'execution d'une action dont l'uid est pass? en param?tre";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULTS;
    
    ExecuteAction(final LuaState luaState, final ActionGroup actionGroup) {
        super(luaState, actionGroup);
    }
    
    @Override
    public String getName() {
        return "executeAction";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return ExecuteAction.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return ExecuteAction.RESULTS;
    }
    
    public void run(final int paramCount) throws LuaException {
        final int uid = this.getParamInt(0);
        final Action action = this.m_actionGroup.getActionByUniqueId(uid);
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
        return "Force l'execution d'une action dont l'uid est pass? en param?tre";
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("actionUID", null, LuaScriptParameterType.INTEGER, false) };
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("actionExists", null, LuaScriptParameterType.BOOLEAN, false) };
    }
}
