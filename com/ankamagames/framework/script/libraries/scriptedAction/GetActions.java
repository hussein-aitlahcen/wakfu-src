package com.ankamagames.framework.script.libraries.scriptedAction;

import com.ankamagames.framework.script.action.*;
import java.util.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class GetActions extends ActionGroupFunction
{
    private static final String NAME = "getActions";
    private static final String DESC = "Retourne des infos (uid, instigatorId, targetId) sur les actions du groupe";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULTS;
    
    GetActions(final LuaState luaState, final ActionGroup actionGroup) {
        super(luaState, actionGroup);
    }
    
    @Override
    public String getName() {
        return "getActions";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return GetActions.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetActions.RESULTS;
    }
    
    public void run(final int paramCount) throws LuaException {
        final int type = this.getParamInt(0);
        final int id = this.getParamInt(1);
        this.L.newTable();
        int actionNumber = 1;
        for (final Action action : this.m_actionGroup.getActions()) {
            if (action.getActionId() == id && action.getActionType() == type) {
                this.L.pushNumber((double)(actionNumber++));
                this.L.newTable();
                this.L.pushString("uid");
                this.L.pushNumber((double)action.getUniqueId());
                this.L.setTable(-3);
                this.L.pushString("from");
                this.L.pushNumber((double)action.getInstigatorId());
                this.L.setTable(-3);
                this.L.pushString("target");
                this.L.pushNumber((double)action.getTargetId());
                this.L.setTable(-3);
                this.L.setTable(-3);
            }
        }
        ++this.m_returnValueCount;
    }
    
    @Override
    public String getDescription() {
        return "Retourne des infos (uid, instigatorId, targetId) sur les actions du groupe";
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("actionType", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("actionId", null, LuaScriptParameterType.INTEGER, false) };
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("actions", null, LuaScriptParameterType.BLOOPS, false) };
    }
}
