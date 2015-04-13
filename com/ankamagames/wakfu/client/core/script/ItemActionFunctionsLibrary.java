package com.ankamagames.wakfu.client.core.script;

import com.ankamagames.wakfu.client.core.action.world.*;
import org.keplerproject.luajava.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import org.jetbrains.annotations.*;

public class ItemActionFunctionsLibrary extends JavaFunctionsLibrary
{
    private static final LuaScriptParameterDescriptor[] GET_CAST_POSITION_RESULTS;
    private final ActorItemAction m_action;
    
    @Override
    public final String getName() {
        return "ItemAction";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    public ItemActionFunctionsLibrary(final ActorItemAction action) {
        super();
        this.m_action = action;
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new GetMobile(luaState), new GetCastPosition(luaState), new SelectPosition(luaState), new SelectCharacter(luaState), new SetClientVar(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    static {
        GET_CAST_POSITION_RESULTS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("posX", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posY", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posZ", null, LuaScriptParameterType.INTEGER, false) };
    }
    
    private class GetMobile extends JavaFunctionEx
    {
        GetMobile(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "getMobile";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return null;
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", null, LuaScriptParameterType.LONG, false) };
        }
        
        public void run(final int paramCount) throws LuaException {
            this.addReturnValue(ItemActionFunctionsLibrary.this.m_action.getCharacter().getActor().getId());
        }
    }
    
    private static class GetCastPosition extends JavaFunctionEx
    {
        GetCastPosition(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "getCastPosition";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return null;
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return ItemActionFunctionsLibrary.GET_CAST_POSITION_RESULTS;
        }
        
        public void run(final int paramCount) throws LuaException {
            final Point3 target = UIWorldItemUseInteractionFrame.getInstance().getLastTarget();
            this.addReturnValue(target.getX());
            this.addReturnValue(target.getY());
            this.addReturnValue(target.getZ());
        }
    }
    
    private static class SelectPosition extends JavaFunctionEx
    {
        SelectPosition(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "selectPosition";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("funcName", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("funcParams", null, LuaScriptParameterType.BLOOPS, true) };
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final String funcName = this.getParamString(0);
            final LuaValue[] arg = this.getParams(1, paramCount);
            final LuaScript script = this.getScriptObject();
            final int size = (arg != null) ? arg.length : 0;
            final LuaValue[] args = new LuaValue[size + 4];
            if (size != 0) {
                System.arraycopy(arg, 0, args, 4, arg.length);
            }
            if (WakfuGameEntity.getInstance().hasFrame(TargetAPSFrame.getInstance())) {
                WakfuGameEntity.getInstance().removeFrame(TargetAPSFrame.getInstance());
            }
            final int taskId = script.registerWaitingTask(funcName, args);
            TargetAPSFrame.getInstance().selectMode((byte)1);
            TargetAPSFrame.getInstance().setSelectionListener(new TargetAPSSelectionListener() {
                @Override
                public void onPositionSelected(@NotNull final Point3 pos) {
                    this.execute(pos);
                }
                
                private void execute(final Point3 pos) {
                    args[0] = new LuaValue(pos.getX());
                    args[1] = new LuaValue(pos.getY());
                    args[2] = new LuaValue(pos.getZ());
                    args[3] = new LuaValue(0);
                    script.executeWaitingTask(taskId);
                }
                
                @Override
                public void onCharacterSelected(@NotNull final CharacterActor character) {
                    this.onSelectionCanceled(null);
                }
                
                @Override
                public void onSelectionCanceled(@Nullable final Point3 pos) {
                    if (pos != null) {
                        this.execute(pos);
                    }
                    else {
                        script.cancelWaitingTask(taskId);
                    }
                }
            });
            WakfuGameEntity.getInstance().pushFrame(TargetAPSFrame.getInstance());
        }
    }
    
    private static class SelectCharacter extends JavaFunctionEx
    {
        SelectCharacter(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "selectCharacter";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("funcName", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("funcParams", null, LuaScriptParameterType.BLOOPS, true) };
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final String funcName = this.getParamString(0);
            final LuaValue[] arg = this.getParams(1, paramCount);
            final LuaScript script = this.getScriptObject();
            final int size = (arg != null) ? arg.length : 0;
            final LuaValue[] args = new LuaValue[size + 1];
            if (size != 0) {
                System.arraycopy(arg, 0, args, 1, arg.length);
            }
            if (WakfuGameEntity.getInstance().hasFrame(TargetAPSFrame.getInstance())) {
                WakfuGameEntity.getInstance().removeFrame(TargetAPSFrame.getInstance());
            }
            final int taskId = script.registerWaitingTask(funcName, args);
            TargetAPSFrame.getInstance().selectMode((byte)2);
            TargetAPSFrame.getInstance().setSelectionListener(new TargetAPSSelectionListener() {
                @Override
                public void onPositionSelected(@NotNull final Point3 pos) {
                    this.onSelectionCanceled(pos);
                }
                
                @Override
                public void onCharacterSelected(@NotNull final CharacterActor character) {
                    args[0] = new LuaValue(character.getId());
                    script.executeWaitingTask(taskId);
                }
                
                @Override
                public void onSelectionCanceled(@Nullable final Point3 pos) {
                    script.cancelWaitingTask(taskId);
                }
            });
            WakfuGameEntity.getInstance().pushFrame(TargetAPSFrame.getInstance());
        }
    }
    
    private class SetClientVar extends JavaFunctionEx
    {
        SetClientVar(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setClientVar";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("varValue", null, LuaScriptParameterType.LONG, false) };
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            ItemActionFunctionsLibrary.this.m_action.setClientVarValue(this.getParamLong(0));
        }
    }
}
