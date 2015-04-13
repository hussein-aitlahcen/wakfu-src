package com.ankamagames.wakfu.client.core.script.fightLibrary.scriptedAction;

import com.ankamagames.framework.script.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class WakfuScriptedActionFunctionsLibrary extends JavaFunctionsLibrary
{
    private static final String NAME = "WakfuScriptedAction";
    private static final String DESC = "Librarie qui fournit des fonctions permettant de forcer l'execution d'actions dans des groupes d'actions de Wakfu";
    private final ActionGroup m_actionGroup;
    
    public WakfuScriptedActionFunctionsLibrary(final ActionGroup actionGroup) {
        super();
        this.m_actionGroup = actionGroup;
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new ExecuteSpellCost(luaState, this.m_actionGroup), new GetNextActionEffectArea(luaState, this.m_actionGroup) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    @Override
    public final String getName() {
        return "WakfuScriptedAction";
    }
    
    @Override
    public String getDescription() {
        return "Librarie qui fournit des fonctions permettant de forcer l'execution d'actions dans des groupes d'actions de Wakfu";
    }
}
