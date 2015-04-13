package com.ankamagames.framework.script.libraries.scriptedAction;

import com.ankamagames.framework.script.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class ScriptedActionFunctionsLibrary extends JavaFunctionsLibrary
{
    private static final String NAME = "ScriptedAction";
    private static final String DESC = "Librarie qui fournit des fonctions permettant de forcer l'execution d'actions dans des groupes d'actions";
    private final ActionGroup m_actionGroup;
    
    @Override
    public final String getName() {
        return "ScriptedAction";
    }
    
    @Override
    public String getDescription() {
        return "Librarie qui fournit des fonctions permettant de forcer l'execution d'actions dans des groupes d'actions";
    }
    
    public ScriptedActionFunctionsLibrary(final ActionGroup actionGroup) {
        super();
        this.m_actionGroup = actionGroup;
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new GetActions(luaState, this.m_actionGroup), new ExecuteAction(luaState, this.m_actionGroup), new ExecuteFirstAction(luaState, this.m_actionGroup), new ExecuteAllAction(luaState, this.m_actionGroup), new GetNextActionTarget(luaState, this.m_actionGroup), new GetNextActionsTargets(luaState, this.m_actionGroup), new GetNextActionWithSpecialIdTarget(luaState, this.m_actionGroup), new ExecuteFirstActionWithSpecialId(luaState, this.m_actionGroup), new ExecuteActionsWithSpecialId(luaState, this.m_actionGroup) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
}
