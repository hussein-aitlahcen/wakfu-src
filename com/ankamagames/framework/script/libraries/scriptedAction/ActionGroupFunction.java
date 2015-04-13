package com.ankamagames.framework.script.libraries.scriptedAction;

import com.ankamagames.framework.script.*;
import com.ankamagames.framework.script.action.*;
import org.keplerproject.luajava.*;

abstract class ActionGroupFunction extends JavaFunctionEx
{
    protected final ActionGroup m_actionGroup;
    
    public ActionGroupFunction(final LuaState luaState, final ActionGroup actionGroup) {
        super(luaState);
        this.m_actionGroup = actionGroup;
    }
}
