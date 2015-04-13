package com.ankamagames.framework.script.libraries.scriptedAction;

import com.ankamagames.framework.script.action.*;
import java.util.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class GetNextActionsTargets extends ActionGroupFunction
{
    private static final String NAME = "getNextActionsTargets";
    private static final String DESC = "Retourne un tableau contenant les ids des cibles des prochaines actions du groupe dont le type est l'id sont pass?s en param?tre";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULTS;
    
    GetNextActionsTargets(final LuaState luaState, final ActionGroup actionGroup) {
        super(luaState, actionGroup);
    }
    
    @Override
    public String getName() {
        return "getNextActionsTargets";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return GetNextActionsTargets.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetNextActionsTargets.RESULTS;
    }
    
    public void run(final int paramCount) throws LuaException {
        final int type = this.getParamInt(0);
        final int id = this.getParamInt(1);
        this.L.newTable();
        int i = 1;
        for (final Action action : this.m_actionGroup.getActions()) {
            if (action.getActionId() == id) {
                if (action.getActionType() != type) {
                    continue;
                }
                this.L.pushNumber((double)(i++));
                this.L.pushObjectValue((Object)action.getTargetId());
                this.L.setTable(-3);
            }
        }
        ++this.m_returnValueCount;
    }
    
    @Override
    public String getDescription() {
        return "Retourne un tableau contenant les ids des cibles des prochaines actions du groupe dont le type est l'id sont pass?s en param?tre";
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("actionType", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("actionId", null, LuaScriptParameterType.INTEGER, false) };
        RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("targetId", null, LuaScriptParameterType.TABLE, false) };
    }
}
