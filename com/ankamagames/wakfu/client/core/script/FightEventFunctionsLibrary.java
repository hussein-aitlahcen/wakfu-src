package com.ankamagames.wakfu.client.core.script;

import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.fight.handler.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.script.*;
import org.keplerproject.luajava.*;

public class FightEventFunctionsLibrary extends JavaFunctionsLibrary
{
    private static final FightEventFunctionsLibrary m_instance;
    private static final LuaScriptParameterDescriptor[] FIGHT_CALLBACK_PARAMS;
    private static final LuaScriptParameterDescriptor[] FIGHTER_CALLBACK_PARAMS;
    private static final LuaScriptParameterDescriptor[] SPELL_CAST_CALLBACK_PARAMS;
    
    public static FightEventFunctionsLibrary getInstance() {
        return FightEventFunctionsLibrary.m_instance;
    }
    
    @Override
    public final String getName() {
        return "FightEvent";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new AddPlacementStartCallback(luaState), new AddPlacementEndCallback(luaState), new AddFightStartCallback(luaState), new AddFightEndCallback(luaState), new AddTableTurnStartCallback(luaState), new AddTableTurnEndCallback(luaState), new AddFighterTurnStartCallback(luaState), new AddFighterTurnEndCallback(luaState), new AddFighterJoinFightCallback(luaState), new AddFighterLeaveFightCallback(luaState), new AddFighterWinFightCallback(luaState), new AddFighterLoseFightCallback(luaState), new SpellCastCallback(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    static {
        m_instance = new FightEventFunctionsLibrary();
        FIGHT_CALLBACK_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("funcName", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("params", null, LuaScriptParameterType.BLOOPS, true) };
        FIGHTER_CALLBACK_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("funcName", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("params", null, LuaScriptParameterType.BLOOPS, true) };
        SPELL_CAST_CALLBACK_PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("spellElement", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("funcName", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("params", null, LuaScriptParameterType.BLOOPS, true) };
    }
    
    private static class FightCallback extends JavaFunctionEx
    {
        private final String m_name;
        private final FightEventType m_eventType;
        
        private FightCallback(final LuaState state, final String name, final FightEventType eventType) {
            super(state);
            this.m_name = name;
            this.m_eventType = eventType;
        }
        
        @Override
        public String getName() {
            return this.m_name;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return FightEventFunctionsLibrary.FIGHT_CALLBACK_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final String functionName = this.getParamString(0);
            final LuaValue[] parameters = this.getParams(1, paramCount);
            final LuaScript script = this.getScriptObject();
            RunnableFightListener.INSTANCE.addEventListener(this.m_eventType, new FightEventRunnable() {
                @Override
                public void runSpellCastEvent(final BasicCharacterInfo caster, final long spellElement) {
                }
                
                @Override
                public void runFighterEvent(final BasicCharacterInfo fighter) {
                }
                
                @Override
                public void runFightEvent() {
                    script.runFunction(functionName, parameters, new LuaTable[0]);
                }
            });
        }
    }
    
    private static class AddPlacementStartCallback extends FightCallback
    {
        AddPlacementStartCallback(final LuaState state) {
            super(state, "addPlacementStartCallback", FightEventType.PLACEMENT_START);
        }
    }
    
    private static class AddPlacementEndCallback extends FightCallback
    {
        AddPlacementEndCallback(final LuaState state) {
            super(state, "addPlacementEndCallback", FightEventType.PLACEMENT_END);
        }
    }
    
    private static class AddFightStartCallback extends FightCallback
    {
        AddFightStartCallback(final LuaState state) {
            super(state, "addFightStartCallback", FightEventType.FIGHT_START);
        }
    }
    
    private static class AddFightEndCallback extends FightCallback
    {
        AddFightEndCallback(final LuaState state) {
            super(state, "addFightEndCallback", FightEventType.FIGHT_ENDED);
        }
    }
    
    private static class AddTableTurnStartCallback extends FightCallback
    {
        AddTableTurnStartCallback(final LuaState state) {
            super(state, "addTableTurnStartCallback", FightEventType.TABLE_TURN_START);
        }
    }
    
    private static class AddTableTurnEndCallback extends FightCallback
    {
        AddTableTurnEndCallback(final LuaState state) {
            super(state, "addTableTurnEndCallback", FightEventType.TABLE_TURN_END);
        }
    }
    
    private static class FighterCallback extends JavaFunctionEx
    {
        private final String m_name;
        private final FightEventType m_eventType;
        
        private FighterCallback(final LuaState state, final String name, final FightEventType eventType) {
            super(state);
            this.m_name = name;
            this.m_eventType = eventType;
        }
        
        @Override
        public String getName() {
            return this.m_name;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return FightEventFunctionsLibrary.FIGHTER_CALLBACK_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long mobileId = this.getParamLong(0);
            final String functionName = this.getParamString(1);
            final LuaValue[] parameters = this.getParams(2, paramCount);
            final LuaScript script = this.getScriptObject();
            RunnableFightListener.INSTANCE.addEventListener(this.m_eventType, new FightEventRunnable() {
                @Override
                public void runSpellCastEvent(final BasicCharacterInfo caster, final long spellElement) {
                }
                
                @Override
                public void runFighterEvent(final BasicCharacterInfo fighter) {
                    if (fighter.getId() == mobileId) {
                        script.runFunction(functionName, parameters, new LuaTable[0]);
                    }
                }
                
                @Override
                public void runFightEvent() {
                }
            });
        }
    }
    
    private static class AddFighterTurnStartCallback extends FighterCallback
    {
        private AddFighterTurnStartCallback(final LuaState state) {
            super(state, "addFighterTurnStartCallback", FightEventType.FIGHTER_TURN_START);
        }
    }
    
    private static class AddFighterTurnEndCallback extends FighterCallback
    {
        private AddFighterTurnEndCallback(final LuaState state) {
            super(state, "addFighterTurnEndCallback", FightEventType.FIGHTER_TURN_END);
        }
    }
    
    private static class AddFighterJoinFightCallback extends FighterCallback
    {
        private AddFighterJoinFightCallback(final LuaState state) {
            super(state, "addFighterJoinFightCallback", FightEventType.FIGHTER_JOIN_FIGHT);
        }
    }
    
    private static class AddFighterLeaveFightCallback extends FighterCallback
    {
        private AddFighterLeaveFightCallback(final LuaState state) {
            super(state, "addFighterLeaveFightCallback", FightEventType.FIGHTER_LEAVE_FIGHT);
        }
    }
    
    private static class AddFighterWinFightCallback extends FighterCallback
    {
        private AddFighterWinFightCallback(final LuaState state) {
            super(state, "addFighterWinFightCallback", FightEventType.FIGHTER_WIN_FIGHT);
        }
    }
    
    private static class AddFighterLoseFightCallback extends FighterCallback
    {
        private AddFighterLoseFightCallback(final LuaState state) {
            super(state, "addFighterLoseFightCallback", FightEventType.FIGHTER_LOSE_FIGHT);
        }
    }
    
    private static class SpellCastCallback extends JavaFunctionEx
    {
        private SpellCastCallback(final LuaState state) {
            super(state);
        }
        
        @Override
        public String getName() {
            return "addSpellCastCallback";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return FightEventFunctionsLibrary.SPELL_CAST_CALLBACK_PARAMS;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long mobileId = this.getParamLong(0);
            final long expectedSpellElement = this.getParamLong(1);
            final String functionName = this.getParamString(2);
            final LuaValue[] parameters = this.getParams(3, paramCount);
            final LuaScript script = this.getScriptObject();
            RunnableFightListener.INSTANCE.addEventListener(FightEventType.FIGHTER_CAST_SPELL, new FightEventRunnable() {
                @Override
                public void runSpellCastEvent(final BasicCharacterInfo caster, final long spellElement) {
                    if (caster.getId() == mobileId && spellElement == expectedSpellElement) {
                        script.runFunction(functionName, parameters, new LuaTable[0]);
                    }
                }
                
                @Override
                public void runFighterEvent(final BasicCharacterInfo fighter) {
                }
                
                @Override
                public void runFightEvent() {
                }
            });
        }
    }
}
