package com.ankamagames.framework.script.libraries.scriptedAction;

import com.ankamagames.framework.script.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class GetNextActionTarget extends ActionGroupFunction
{
    private static final String NAME = "getNextActionTarget";
    private static final String DESC = "Retourne l'id de la cible de la prochaine action du groupe dont le type est l'id sont pass?s en param?tre";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULTS;
    
    GetNextActionTarget(final LuaState luaState, final ActionGroup actionGroup) {
        super(luaState, actionGroup);
    }
    
    @Override
    public String getName() {
        return "getNextActionTarget";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return GetNextActionTarget.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetNextActionTarget.RESULTS;
    }
    
    public void run(final int paramCount) throws LuaException {
        final int type = this.getParamInt(0);
        final int id = this.getParamInt(1);
        final Action action = this.m_actionGroup.getActionByTypeAndId(type, id);
        if (action != null) {
            this.addReturnValue(action.getTargetId());
        }
        else {
            this.addReturnValue(0L);
        }
    }
    
    @Override
    public String getDescription() {
        return "Retourne l'id de la cible de la prochaine action du groupe dont le type est l'id sont pass?s en param?tre";
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("actionType", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("actionId", null, LuaScriptParameterType.INTEGER, false) };
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("targetId", null, LuaScriptParameterType.LONG, false) };
    }
}
