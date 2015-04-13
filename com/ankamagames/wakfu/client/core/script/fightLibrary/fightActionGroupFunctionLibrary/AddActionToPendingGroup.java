package com.ankamagames.wakfu.client.core.script.fightLibrary.fightActionGroupFunctionLibrary;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.script.action.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

final class AddActionToPendingGroup extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "addActionToPendingGroup";
    private static final String DESC = "Permet d'ajouter une action au groupe d'actions du combat du joueur et qui va executer la fonction pass?e en param?tre sur une certaine dur?e";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    AddActionToPendingGroup(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "addActionToPendingGroup";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return AddActionToPendingGroup.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long fighterId = this.getParamLong(0);
        final long duration = this.getParamLong(1);
        final String functionName = this.getParamString(2);
        final LuaValue[] parameters = this.getParams(3, paramCount);
        final LuaScript script = this.getScriptObject();
        final CharacterInfo fighter = CharacterInfoManager.getInstance().getCharacter(fighterId);
        if (fighter == null) {
            AddActionToPendingGroup.m_logger.warn((Object)("Impossible d'executer la fonction, Fighter inconnu : " + fighterId));
            return;
        }
        final Fight currentFight = fighter.getCurrentFight();
        if (currentFight == null) {
            AddActionToPendingGroup.m_logger.warn((Object)("Impossible d'executer la fonction, le fighter n'est pas en combat " + fighterId));
            return;
        }
        final Action action = new TimedAction(TimedAction.getNextUid(), FightActionType.FROM_SCRIPT_ACTION.getId(), 0) {
            @Override
            protected long onRun() {
                script.runFunction(functionName, parameters, new LuaTable[0]);
                return duration;
            }
            
            @Override
            protected void onActionFinished() {
            }
        };
        FightActionGroupManager.getInstance().addActionToPendingGroup(currentFight, action);
        FightActionGroupManager.getInstance().executePendingGroup(currentFight);
    }
    
    @Override
    public String getDescription() {
        return "Permet d'ajouter une action au groupe d'actions du combat du joueur et qui va executer la fonction pass?e en param?tre sur une certaine dur?e";
    }
    
    static {
        m_logger = Logger.getLogger((Class)AddActionToPendingGroup.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("fighterId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("actionDuration", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("functionName", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("functionParams", null, LuaScriptParameterType.BLOOPS, true) };
    }
}
