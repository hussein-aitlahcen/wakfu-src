package com.ankamagames.framework.script.libraries.scriptedAction;

import com.ankamagames.framework.script.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class GetNextActionWithSpecialIdTarget extends ActionGroupFunction
{
    private static final String NAME = "getNextActionWithSpecialIdTarget";
    private static final String DESC = "Retourne l'id de la cible de la prochaine action du groupe dont l'id (id de l'effet, pas du type de l'effet) est pass? en param?tre";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULTS;
    
    GetNextActionWithSpecialIdTarget(final LuaState luaState, final ActionGroup actionGroup) {
        super(luaState, actionGroup);
    }
    
    @Override
    public String getName() {
        return "getNextActionWithSpecialIdTarget";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return GetNextActionWithSpecialIdTarget.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetNextActionWithSpecialIdTarget.RESULTS;
    }
    
    public void run(final int paramCount) throws LuaException {
        final int id = this.getParamInt(0);
        final Action action = this.m_actionGroup.getActionsBySpecialId(id);
        if (action != null) {
            this.addReturnValue(action.getTargetId());
        }
        else {
            this.addReturnValue(0L);
        }
    }
    
    @Override
    public String getDescription() {
        return "Retourne l'id de la cible de la prochaine action du groupe dont l'id (id de l'effet, pas du type de l'effet) est pass? en param?tre";
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("specialId", null, LuaScriptParameterType.INTEGER, false) };
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("targetId", null, LuaScriptParameterType.LONG, false) };
    }
}
