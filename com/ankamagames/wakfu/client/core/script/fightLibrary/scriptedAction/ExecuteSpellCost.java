package com.ankamagames.wakfu.client.core.script.fightLibrary.scriptedAction;

import com.ankamagames.framework.script.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.common.game.effect.*;
import org.keplerproject.luajava.*;
import com.ankamagames.wakfu.common.game.fight.*;

class ExecuteSpellCost extends JavaFunctionEx
{
    private static final int TYPE;
    private static final String NAME = "executeSpellCost";
    private static final String DESC = "Execute les effets de type ActionCost pr?sents dans le groupe d'actions";
    private final ActionGroup m_actionGroup;
    
    public ExecuteSpellCost(final LuaState luaState, final ActionGroup actionGroup) {
        super(luaState);
        this.m_actionGroup = actionGroup;
    }
    
    @Override
    public final String getName() {
        return "executeSpellCost";
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("actionExists", null, LuaScriptParameterType.BOOLEAN, false) };
    }
    
    public final void run(final int paramCount) throws LuaException {
        boolean actionExists = false;
        final Action[] actions = new Action[this.m_actionGroup.getActions().size()];
        this.m_actionGroup.getActions().toArray(actions);
        for (final Action action : actions) {
            if (action.getActionType() == ExecuteSpellCost.TYPE) {
                final int actionId = action.getActionId();
                if (actionId == RunningEffectConstants.ACTION_COST.getId()) {
                    this.m_actionGroup.runAction(action, false);
                    actionExists = true;
                }
            }
        }
        this.addReturnValue(actionExists);
    }
    
    @Override
    public String getDescription() {
        return "Execute les effets de type ActionCost pr?sents dans le groupe d'actions";
    }
    
    static {
        TYPE = FightActionType.EFFECT_EXECUTION.getId();
    }
}
