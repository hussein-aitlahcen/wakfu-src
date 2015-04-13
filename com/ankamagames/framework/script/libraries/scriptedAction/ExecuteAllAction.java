package com.ankamagames.framework.script.libraries.scriptedAction;

import com.ankamagames.framework.script.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class ExecuteAllAction extends ActionGroupFunction
{
    private static final String NAME = "executeAllAction";
    private static final String DESC = "Execute toutes les actions dont le type est l'id sont pass?s en param?tre";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    ExecuteAllAction(final LuaState luaState, final ActionGroup actionGroup) {
        super(luaState, actionGroup);
    }
    
    @Override
    public String getName() {
        return "executeAllAction";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return ExecuteAllAction.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final int type = this.getParamInt(0);
        final int id = this.getParamInt(1);
        for (Action action = this.m_actionGroup.getActionByTypeAndId(type, id); action != null; action = this.m_actionGroup.getActionByTypeAndId(type, id)) {
            this.m_actionGroup.runAction(action, false);
        }
    }
    
    @Override
    public String getDescription() {
        return "Execute toutes les actions dont le type est l'id sont pass?s en param?tre";
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("actionType", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("actionId", null, LuaScriptParameterType.INTEGER, false) };
    }
}
