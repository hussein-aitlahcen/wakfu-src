package com.ankamagames.wakfu.client.core.script;

import org.apache.log4j.*;
import java.util.*;
import org.keplerproject.luajava.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.baseImpl.client.proxyclient.base.game.interactiveElement.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;

public class InteractiveElementFunctionsLibrary extends JavaFunctionsLibrary
{
    private static final Logger m_logger;
    private static final InteractiveElementFunctionsLibrary m_instance;
    
    @Override
    public final String getName() {
        return "InteractiveElement";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    public static InteractiveElementFunctionsLibrary getInstance() {
        return InteractiveElementFunctionsLibrary.m_instance;
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new SetAnimation(luaState), new GetElementId(luaState), new GetElementsId(luaState), new FireAction(luaState), new SetVisible(luaState), new IsVisible(luaState), new GetPosition(luaState), new GetState(luaState), new IsUsable(luaState), new SetState(luaState), new SetBlockingMovements(luaState), new AddInteractiveElementCreationCallback(luaState), new AddInteractiveElementDestructionCallback(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)InteractiveElementFunctionsLibrary.class);
        m_instance = new InteractiveElementFunctionsLibrary();
    }
    
    private static class GetElementId extends JavaFunctionEx
    {
        GetElementId(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "getElementId";
        }
        
        @Override
        public String getDescription() {
            return "R?cup?re l'id d'un element interactif ? une position";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("type", "Type de l'element interactif", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posX", "Position x", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posY", "Position y", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posZ", "Position z", LuaScriptParameterType.INTEGER, true) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("id", null, LuaScriptParameterType.LONG, false) };
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final int type = this.getParamInt(0);
            final int x = this.getParamInt(1);
            final int y = this.getParamInt(2);
            final ArrayList<ClientInteractiveAnimatedElementSceneView> elements = AnimatedElementSceneViewManager.getInstance().getElements(x, y);
            final int elementCount = elements.size();
            if (elementCount == 0) {
                this.addReturnNilValue();
                return;
            }
            if (paramCount < 4) {
                for (int i = 0; i < elementCount; ++i) {
                    final ClientInteractiveAnimatedElementSceneView elementSceneView = elements.get(i);
                    if (elementSceneView.getViewGfxId() == type) {
                        this.addReturnValue(elementSceneView.getId());
                        return;
                    }
                }
            }
            else {
                final int z = this.getParamInt(3);
                for (int j = 0; j < elementCount; ++j) {
                    final ClientInteractiveAnimatedElementSceneView elementSceneView2 = elements.get(j);
                    if (elementSceneView2.getViewGfxId() == type && elementSceneView2.getAltitude() == z) {
                        this.addReturnValue(elementSceneView2.getId());
                        return;
                    }
                }
            }
            this.writeError(InteractiveElementFunctionsLibrary.m_logger, "L'element interactif de type=" + type + " en (" + x + "," + y + ") n'existe pas");
        }
    }
    
    private static class GetElementsId extends JavaFunctionEx
    {
        GetElementsId(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "getElementsId";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("type", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posX", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posY", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("posZ", null, LuaScriptParameterType.INTEGER, true) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("id", null, LuaScriptParameterType.LONG, false) };
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final int type = this.getParamInt(0);
            final int x = this.getParamInt(1);
            final int y = this.getParamInt(2);
            final ArrayList<ClientInteractiveAnimatedElementSceneView> elements = AnimatedElementSceneViewManager.getInstance().getElements(x, y);
            final int elementCount = elements.size();
            boolean found = false;
            if (elementCount != 0) {
                if (paramCount < 4) {
                    for (int i = 0; i < elementCount; ++i) {
                        final ClientInteractiveAnimatedElementSceneView elementSceneView = elements.get(i);
                        if (elementSceneView.getViewGfxId() == type) {
                            this.addReturnValue(elementSceneView.getId());
                            found = true;
                        }
                    }
                }
                else {
                    final int z = this.getParamInt(3);
                    for (int j = 0; j < elementCount; ++j) {
                        final ClientInteractiveAnimatedElementSceneView elementSceneView2 = elements.get(j);
                        if (elementSceneView2.getViewGfxId() == type && elementSceneView2.getAltitude() == z) {
                            this.addReturnValue(elementSceneView2.getId());
                            found = true;
                        }
                    }
                }
            }
            if (!found) {
                this.addReturnNilValue();
                this.writeError(InteractiveElementFunctionsLibrary.m_logger, "L'element interactif de type=" + type + " en (" + x + "," + y + ") n'existe pas");
            }
        }
    }
    
    private static class SetAnimation extends JavaFunctionEx
    {
        SetAnimation(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setAnimation";
        }
        
        @Override
        public String getDescription() {
            return "Joue l'animation donn?e pour un element interactif";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("id", "Id de l'element interactif", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("animationName", "Nom de lanimation", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("func", null, LuaScriptParameterType.STRING, true), new LuaScriptParameterDescriptor("params", null, LuaScriptParameterType.BLOOPS, true) };
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final long id = this.getParamLong(0);
            final String animation = this.getParamString(1);
            final ClientInteractiveAnimatedElementSceneView elementView = AnimatedElementSceneViewManager.getInstance().getElement(id);
            if (elementView == null) {
                this.writeError(InteractiveElementFunctionsLibrary.m_logger, "l'?l?ment d'ID " + id + " n'existe pas");
                return;
            }
            elementView.setAnimation(animation);
            if (paramCount <= 2) {
                return;
            }
            final LuaScript script = this.getScriptObject();
            final String func = this.getParamString(2);
            final LuaValue[] params = this.getParams(3, paramCount);
            final int taskId = script.registerWaitingTask(func, params);
            elementView.addAnimationEndedListener(new AnimationEndedListener() {
                @Override
                public void animationEnded(final AnimatedElement element) {
                    elementView.removeAnimationEndedListener(this);
                    script.executeWaitingTask(taskId);
                }
            });
        }
    }
    
    private static class SetVisible extends JavaFunctionEx
    {
        SetVisible(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setVisible";
        }
        
        @Override
        public String getDescription() {
            return "Change la visibilit? d'un element interactif";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("id", "Id de l'element interactif", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("visible", "Visibilit?", LuaScriptParameterType.BOOLEAN, false) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long id = this.getParamLong(0);
            final boolean visible = this.getParamBool(1);
            final ClientInteractiveAnimatedElementSceneView elementView = AnimatedElementSceneViewManager.getInstance().getElement(id);
            if (elementView == null) {
                this.writeError(InteractiveElementFunctionsLibrary.m_logger, "l'element interactif d'id " + id + " n'existe pas.");
                return;
            }
            elementView.setVisible(visible);
        }
    }
    
    private static class FireAction extends JavaFunctionEx
    {
        FireAction(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "fireAction";
        }
        
        @Override
        public String getDescription() {
            return "Execute une action pour un element interactif";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("interactiveElementId", "Id de l'element interactif", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("action", "Action ? executer", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("user", "Id unique du personnage", LuaScriptParameterType.LONG, false) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("executed", "Si l'action ? bien ?t? execut?", LuaScriptParameterType.BOOLEAN, false) };
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long id = this.getParamLong(0);
            final String actionName = this.getParamString(1);
            final long userId = this.getParamLong(2);
            final ClientInteractiveAnimatedElementSceneView elementView = AnimatedElementSceneViewManager.getInstance().getElement(id);
            try {
                final InteractiveElementUser user = CharacterInfoManager.getInstance().getCharacter(userId);
                final InteractiveElementAction action = InteractiveElementAction.valueOf(actionName);
                final boolean executed = elementView.getInteractiveElement().fireAction(action, user);
                this.addReturnValue(executed);
            }
            catch (IllegalArgumentException e) {
                this.writeError(InteractiveElementFunctionsLibrary.m_logger, "type d'action inconnue " + actionName);
                this.addReturnNilValue();
            }
        }
    }
    
    private static class IsVisible extends JavaFunctionEx
    {
        IsVisible(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "isVisible";
        }
        
        @Override
        public String getDescription() {
            return "Permet de savoir si un element interactif est 'visible'";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("elementId", "Id de l'element interactif", LuaScriptParameterType.LONG, false) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("visible", "Si l'element est 'visible'", LuaScriptParameterType.BOOLEAN, false) };
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long id = this.getParamLong(0);
            final ClientInteractiveAnimatedElementSceneView elementView = AnimatedElementSceneViewManager.getInstance().getElement(id);
            if (elementView == null) {
                this.writeError(InteractiveElementFunctionsLibrary.m_logger, "l'element interactif d'id " + id + " n'existe pas..");
                this.addReturnNilValue();
                return;
            }
            this.addReturnValue(elementView.isVisible());
        }
    }
    
    private static class GetPosition extends JavaFunctionEx
    {
        GetPosition(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "getPosition";
        }
        
        @Override
        public String getDescription() {
            return "R?cup?re la position d'un element interactif";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("elementId", "Id de l'element interactif", LuaScriptParameterType.LONG, false) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("x", "Position x", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("y", "Position y", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("z", "Position z", LuaScriptParameterType.INTEGER, false) };
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long id = this.getParamLong(0);
            final ClientMapInteractiveElement interactiveElement = LocalPartitionManager.getInstance().getInteractiveElement(id);
            if (interactiveElement == null) {
                this.writeError(InteractiveElementFunctionsLibrary.m_logger, this.getName() + " : l'element interactif d'id " + id + " n'existe pas...");
                this.addReturnNilValue();
                this.addReturnNilValue();
                this.addReturnNilValue();
                return;
            }
            final Point3 pos = interactiveElement.getPosition();
            this.addReturnValue(pos.getX());
            this.addReturnValue(pos.getY());
            this.addReturnValue(pos.getZ());
        }
    }
    
    private static class GetState extends JavaFunctionEx
    {
        GetState(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "getState";
        }
        
        @Override
        public String getDescription() {
            return "R?cup?re l'?tat d'un element interactif";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("elementId", "Id de l'element interactif", LuaScriptParameterType.LONG, false) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("state", "L'etat de l'element interactif", LuaScriptParameterType.INTEGER, false) };
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long id = this.getParamLong(0);
            final ClientInteractiveAnimatedElementSceneView elementView = AnimatedElementSceneViewManager.getInstance().getElement(id);
            if (elementView == null) {
                this.writeError(InteractiveElementFunctionsLibrary.m_logger, "l'element interactif d'id " + id + " n'existe pas....");
                this.addReturnNilValue();
                return;
            }
            final WakfuClientMapInteractiveElement element = (WakfuClientMapInteractiveElement)elementView.getInteractiveElement();
            this.addReturnValue(element.getState());
        }
    }
    
    private static class SetState extends JavaFunctionEx
    {
        SetState(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setState";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("elementId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("state", null, LuaScriptParameterType.INTEGER, false) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long id = this.getParamLong(0);
            final short state = (short)this.getParamInt(1);
            final ClientMapInteractiveElement ie = LocalPartitionManager.getInstance().getInteractiveElement(id);
            if (ie == null) {
                InteractiveElementFunctionsLibrary.m_logger.error((Object)("L'?l?ment interactif " + id + "n'existe pas"));
                return;
            }
            ie.setState(state);
            ie.notifyChangesListeners();
            ie.notifyViews();
        }
    }
    
    private static class SetBlockingMovements extends JavaFunctionEx
    {
        SetBlockingMovements(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setBlockingMovements";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("elementId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("blocking", null, LuaScriptParameterType.BOOLEAN, false) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long id = this.getParamLong(0);
            final boolean blocking = this.getParamBool(1);
            final ClientMapInteractiveElement ie = LocalPartitionManager.getInstance().getInteractiveElement(id);
            if (ie == null) {
                InteractiveElementFunctionsLibrary.m_logger.error((Object)("L'?l?ment interactif " + id + "n'existe pas"));
                return;
            }
            ie.setBlockingMovements(blocking);
            ie.notifyChangesListeners();
            ie.notifyViews();
        }
    }
    
    private static class IsUsable extends JavaFunctionEx
    {
        IsUsable(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "isUsable";
        }
        
        @Override
        public String getDescription() {
            return "Permet de savoir si un element interactif est 'usable' ou non";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("elementId", "Id de l'element interactif", LuaScriptParameterType.LONG, false) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("usable", "Si l'element interactif est 'usable'", LuaScriptParameterType.BOOLEAN, false) };
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final long id = this.getParamLong(0);
            final ClientInteractiveAnimatedElementSceneView elementView = AnimatedElementSceneViewManager.getInstance().getElement(id);
            if (elementView == null) {
                this.writeError(InteractiveElementFunctionsLibrary.m_logger, "l'element interactif d'id " + id + " n'existe pas.....");
                this.addReturnNilValue();
                return;
            }
            final WakfuClientMapInteractiveElement element = (WakfuClientMapInteractiveElement)elementView.getInteractiveElement();
            this.addReturnValue(element.isUsable());
        }
    }
    
    private static class AddInteractiveElementCreationCallback extends JavaFunctionEx
    {
        AddInteractiveElementCreationCallback(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "addIECreationCallback";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("ie ID", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("Function name", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("Parameters", null, LuaScriptParameterType.BLOOPS, true) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final LuaScript script = this.getScriptObject();
            final int ieId = this.getParamInt(0);
            final String function = this.getParamString(1);
            final LuaValue[] parameters = this.getParams(2, paramCount);
            if (AnimatedElementSceneViewManager.getInstance().getElement(ieId) != null) {
                script.runFunction(function, parameters, new LuaTable[0]);
            }
            AnimatedElementSceneViewManager.getInstance().addCreationListener(new InteractiveElementCreationListener() {
                @Override
                public void onInteractiveElementCreation(final ClientInteractiveAnimatedElementSceneView ie) {
                    if (ie.getInteractiveElement().getId() == ieId) {
                        script.runFunction(function, parameters, new LuaTable[0]);
                    }
                }
            });
        }
    }
    
    private static class AddInteractiveElementDestructionCallback extends JavaFunctionEx
    {
        AddInteractiveElementDestructionCallback(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "addIEDestructionCallback";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("ie ID", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("Function name", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("Parameters", null, LuaScriptParameterType.BLOOPS, true) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final LuaScript script = this.getScriptObject();
            final int ieId = this.getParamInt(0);
            final String function = this.getParamString(1);
            final LuaValue[] parameters = this.getParams(2, paramCount);
            AnimatedElementSceneViewManager.getInstance().addDestructionListener(new InteractiveElementDestructionListener() {
                @Override
                public void onInteractiveElementDestruction(final ClientInteractiveAnimatedElementSceneView ie) {
                    if (ie.getInteractiveElement().getId() == ieId) {
                        script.runFunction(function, parameters, new LuaTable[0]);
                    }
                }
            });
        }
    }
}
