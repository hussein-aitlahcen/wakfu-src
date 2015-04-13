package com.ankamagames.wakfu.client.core.script.fightLibrary.scriptedAction;

import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.client.core.script.fightLibrary.spellEffect.*;
import com.ankamagames.framework.script.action.*;
import java.util.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

class GetNextActionEffectArea extends JavaFunctionEx
{
    private static final LuaScriptParameterDescriptor[] GET_NEXT_ACTION_EFFECT_AREA_PARAMS;
    private static final LuaScriptParameterDescriptor[] GET_NEXT_ACTION_EFFECT_AREA_RESULTS;
    private static final String NAME = "getNextActionEffectArea";
    private static final String DESC = "Permet de r?cup?rer la liste des cellules de la zone d'un effet";
    private final ActionGroup m_actionGroup;
    
    GetNextActionEffectArea(final LuaState luaState, final ActionGroup actionGroup) {
        super(luaState);
        this.m_actionGroup = actionGroup;
    }
    
    @Override
    public final String getName() {
        return "getNextActionEffectArea";
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return GetNextActionEffectArea.GET_NEXT_ACTION_EFFECT_AREA_PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return GetNextActionEffectArea.GET_NEXT_ACTION_EFFECT_AREA_RESULTS;
    }
    
    public void run(final int paramCount) throws LuaException {
        final int type = FightActionType.EFFECT_EXECUTION.getId();
        final int id = this.getParamInt(0);
        final Action action = this.m_actionGroup.getActionByTypeAndId(type, id);
        if (!(action instanceof SpellEffectActionInterface)) {
            this.addReturnValue(0);
            return;
        }
        final Iterable<int[]> iterable = GetEffectArea.extractCells((SpellEffectActionInterface)action);
        if (iterable == null) {
            this.addReturnValue(0);
            return;
        }
        Iterator<int[]> iterator = iterable.iterator();
        int cells = 0;
        while (iterator.hasNext()) {
            iterator.next();
            ++cells;
        }
        this.addReturnValue(cells);
        iterator = iterable.iterator();
        this.L.newTable();
        int i = 1;
        while (iterator.hasNext()) {
            final int[] cell = iterator.next();
            this.L.pushNumber((double)(i++));
            this.L.newTable();
            this.L.pushString("x");
            this.L.pushNumber((double)cell[0]);
            this.L.setTable(-3);
            this.L.pushString("y");
            this.L.pushNumber((double)cell[1]);
            this.L.setTable(-3);
            this.L.setTable(-3);
        }
        ++this.m_returnValueCount;
    }
    
    @Override
    public String getDescription() {
        return "Permet de r?cup?rer la liste des cellules de la zone d'un effet";
    }
    
    static {
        GET_NEXT_ACTION_EFFECT_AREA_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("actionId", null, LuaScriptParameterType.INTEGER, false) };
        GET_NEXT_ACTION_EFFECT_AREA_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("targetId", null, LuaScriptParameterType.INTEGER, false) };
    }
}
