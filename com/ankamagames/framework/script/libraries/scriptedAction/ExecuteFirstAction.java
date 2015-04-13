package com.ankamagames.framework.script.libraries.scriptedAction;

import com.ankamagames.framework.script.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class ExecuteFirstAction extends ActionGroupFunction
{
    private static final String NAME = "executeFirstAction";
    private static final String DESC = "Execute la premiere action de type et d'id donn? dans le groupe d'actions";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULTS;
    
    ExecuteFirstAction(final LuaState luaState, final ActionGroup actionGroup) {
        super(luaState, actionGroup);
    }
    
    @Override
    public String getName() {
        return "executeFirstAction";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return ExecuteFirstAction.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return ExecuteFirstAction.RESULTS;
    }
    
    public void run(final int paramCount) throws LuaException {
        final int type = this.getParamInt(0);
        final int id = this.getParamInt(1);
        final Action action = this.m_actionGroup.getActionByTypeAndId(type, id);
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
        return "Execute la premiere action de type et d'id donn? dans le groupe d'actions";
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("actionType", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("actionId", null, LuaScriptParameterType.INTEGER, false) };
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("actionExists", null, LuaScriptParameterType.BOOLEAN, false) };
    }
}
