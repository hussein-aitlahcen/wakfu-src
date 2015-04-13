package com.ankamagames.wakfu.client.core.script;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.framework.script.*;
import org.keplerproject.luajava.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.framework.script.events.*;

public class EventFunctionsLibrary extends JavaFunctionsLibrary
{
    private static final EventFunctionsLibrary m_instance;
    
    public static EventFunctionsLibrary getInstance() {
        return EventFunctionsLibrary.m_instance;
    }
    
    @Override
    public final String getName() {
        return "Event";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new RunOnInstanceLoaded(luaState), new RunOnInteractiveElementActivation(luaState), new RemoveInteractiveElementListener(luaState), new RunOnItemEquipped(luaState), new RunOnBagLinked(luaState), new AddFleaModifiedListener(luaState), new RemoveFleaModifiedListener(luaState), new RunOnEnterSellerMode(luaState), new SetFightTurnStartedListener(luaState), new RemoveFightTurnStartedListener(luaState), new ListenInteractiveElementActivation(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    static {
        m_instance = new EventFunctionsLibrary();
    }
    
    private static class RunOnInstanceLoaded extends JavaFunctionEx
    {
        public RunOnInstanceLoaded(final LuaState state) {
            super(state);
        }
        
        @Override
        public String getName() {
            return "runOnInstanceLoaded";
        }
        
        @Override
        public String getDescription() {
            return "Ex?cute la fonction pass?e en param?tre d?s que l'instance a fini de charger";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("funcName", "Nom de la fonction", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("params", "Param?tres de la fonction", LuaScriptParameterType.BLOOPS, true) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final AleaWorldScene scene = WakfuClientInstance.getInstance().getWorldScene();
            if (scene != null) {
                final IsoCamera camera = scene.getIsoCamera();
                camera.setTrackingTarget(WakfuGameEntity.getInstance().getLocalPlayer().getActor());
                final LuaScript script = this.getScriptObject();
                final String func = this.getParamString(0);
                final LuaValue[] params = this.getParams(1, paramCount);
                final int taskId = script.registerWaitingTask(func, params);
                camera.setTrackingTarget(camera.getTrackingTarget());
                camera.addCameraReachTargetListener(new ReachTargetListener() {
                    @Override
                    public void onTargetReached() {
                        camera.removeCameraReachTargetListener(this);
                        script.executeWaitingTask(taskId);
                    }
                });
            }
            else {
                this.writeError(EventFunctionsLibrary.m_logger, "pas de scene");
            }
        }
    }
    
    private static class RunOnInteractiveElementActivation extends JavaFunctionEx
    {
        public RunOnInteractiveElementActivation(final LuaState state) {
            super(state);
        }
        
        @Override
        public String getName() {
            return "runOnInteractiveElementActivation";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("elementId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("actionType", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("funcName", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("params", null, LuaScriptParameterType.BLOOPS, true) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long elementId = this.getParamLong(0);
            final short actionId = InteractiveElementAction.valueOf(this.getParamString(1)).getActionId();
            final FilterableEvent event = new InteractiveElementActivated(elementId, actionId);
            final String funcName = this.getParamString(2);
            final LuaValue[] params = this.getParams(3, paramCount);
            final LuaScript script = this.getScriptObject();
            ScriptEventManager.getInstance().addLuaFunction(event, script, funcName, params, true);
        }
    }
    
    private static class ListenInteractiveElementActivation extends JavaFunctionEx
    {
        public ListenInteractiveElementActivation(final LuaState state) {
            super(state);
        }
        
        @Override
        public String getName() {
            return "runOnEachInteractiveElementActivation";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("elementId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("actionType", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("funcName", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("params", null, LuaScriptParameterType.BLOOPS, true) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long elementId = this.getParamLong(0);
            final short actionId = InteractiveElementAction.valueOf(this.getParamString(1)).getActionId();
            final FilterableEvent event = new InteractiveElementActivated(elementId, actionId);
            final String funcName = this.getParamString(2);
            final LuaValue[] params = this.getParams(3, paramCount);
            final LuaScript script = this.getScriptObject();
            ScriptEventManager.getInstance().addLuaFunction(event, script, funcName, params, false);
        }
    }
    
    private static class RemoveInteractiveElementListener extends JavaFunctionEx
    {
        public RemoveInteractiveElementListener(final LuaState state) {
            super(state);
        }
        
        @Override
        public String getName() {
            return "removeInteractiveElementActivationListener";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("elementId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("actionType", null, LuaScriptParameterType.STRING, false) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long elementId = this.getParamLong(0);
            final short actionId = InteractiveElementAction.valueOf(this.getParamString(1)).getActionId();
            final FilterableEvent event = new InteractiveElementActivated(elementId, actionId);
            final LuaScript script = this.getScriptObject();
            ScriptEventManager.getInstance().removeScriptFromEvent(event, script);
        }
    }
    
    private static class RunOnItemEquipped extends JavaFunctionEx
    {
        public RunOnItemEquipped(final LuaState state) {
            super(state);
        }
        
        @Override
        public String getName() {
            return "runOnItemEquipped";
        }
        
        @Override
        public String getDescription() {
            return "Ex?cute la fonction pass?e en param?tre d?s qu'un objet de referenceID donn? est ?quipp?. Le callback se d?senregistre automatiquement une fois ?x?cut?.";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("itemReferenceId", "Reference Id a ?couter", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("funcName", "Focntion ? appeler", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("params", "Param?tres de la fonction ? appeler", LuaScriptParameterType.BLOOPS, true) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final int itemReferenceId = this.getParamInt(0);
            final FilterableEvent event = new ItemEquippedScriptEvent(itemReferenceId);
            final String funcName = this.getParamString(1);
            final LuaValue[] params = this.getParams(2, paramCount);
            final LuaScript script = this.getScriptObject();
            ScriptEventManager.getInstance().addLuaFunction(event, script, funcName, params, true);
        }
    }
    
    private static class RunOnBagLinked extends JavaFunctionEx
    {
        public RunOnBagLinked(final LuaState state) {
            super(state);
        }
        
        @Override
        public String getName() {
            return "runOnBagLinked";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("funcName", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("params", null, LuaScriptParameterType.BLOOPS, true) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final FilterableEvent event = new BagLinkedScriptEvent();
            final String funcName = this.getParamString(0);
            final LuaValue[] params = this.getParams(1, paramCount);
            final LuaScript script = this.getScriptObject();
            ScriptEventManager.getInstance().addLuaFunction(event, script, funcName, params, true);
        }
    }
    
    private static class RunOnEnterSellerMode extends JavaFunctionEx
    {
        public RunOnEnterSellerMode(final LuaState state) {
            super(state);
        }
        
        @Override
        public String getName() {
            return "runOnEnterSellerMode";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("funcName", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("params", null, LuaScriptParameterType.BLOOPS, true) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final FilterableEvent event = new EnterSellerModeScriptEvent();
            final String funcName = this.getParamString(0);
            final LuaValue[] params = this.getParams(1, paramCount);
            final LuaScript script = this.getScriptObject();
            ScriptEventManager.getInstance().addLuaFunction(event, script, funcName, params, true);
        }
    }
    
    private static class AddFleaModifiedListener extends JavaFunctionEx
    {
        public AddFleaModifiedListener(final LuaState state) {
            super(state);
        }
        
        @Override
        public String getName() {
            return "addFleaModifiedListener";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("funcName", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("params", null, LuaScriptParameterType.BLOOPS, true) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final FilterableEvent event = new FleaModifiedScriptEvent();
            final String funcName = this.getParamString(0);
            final LuaValue[] params = this.getParams(1, paramCount);
            final LuaScript script = this.getScriptObject();
            ScriptEventManager.getInstance().addLuaFunction(event, script, funcName, params, false);
        }
    }
    
    private static class RemoveFleaModifiedListener extends JavaFunctionEx
    {
        public RemoveFleaModifiedListener(final LuaState state) {
            super(state);
        }
        
        @Override
        public String getName() {
            return "removeFleaModifiedListener";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return null;
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final FilterableEvent event = new FleaModifiedScriptEvent();
            ScriptEventManager.getInstance().removeLUAFunctions(event);
        }
    }
    
    private static class SetFightTurnStartedListener extends JavaFunctionEx
    {
        public SetFightTurnStartedListener(final LuaState state) {
            super(state);
        }
        
        @Override
        public String getName() {
            return "setFightTurnStartedListener";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("playerId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("funcName", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("params", null, LuaScriptParameterType.BLOOPS, true) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long playerId = this.getParamLong(0);
            final FilterableEvent event = new FightTurnStartedScriptEvent(playerId);
            final String funcName = this.getParamString(1);
            final LuaValue[] params = this.getParams(2, paramCount);
            final LuaScript script = this.getScriptObject();
            ScriptEventManager.getInstance().addLuaFunction(event, script, funcName, params, false);
        }
    }
    
    private static class RemoveFightTurnStartedListener extends JavaFunctionEx
    {
        public RemoveFightTurnStartedListener(final LuaState state) {
            super(state);
        }
        
        @Override
        public String getName() {
            return "removeFightTurnStartedListener";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("playerId", null, LuaScriptParameterType.INTEGER, false) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final int playerId = this.getParamInt(0);
            final FilterableEvent event = new FightTurnStartedScriptEvent(playerId);
            ScriptEventManager.getInstance().removeLUAFunctions(event);
        }
    }
}
