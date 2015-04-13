package com.ankamagames.wakfu.client.core.script;

import org.apache.log4j.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;

public class ResourceFunctionsLibrary extends JavaFunctionsLibrary
{
    public static final ResourceFunctionsLibrary INSTANCE;
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new SetResourceVisible(luaState), new IsResourceVisible(luaState), new SetResourceAlpha(luaState), new GetResourceAlpha(luaState), new AddResourceCreationCallback(luaState), new AddResourceDestructionCallback(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    @Override
    public final String getName() {
        return "Resource";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    static {
        INSTANCE = new ResourceFunctionsLibrary();
    }
    
    private static class SetResourceVisible extends JavaFunctionEx
    {
        SetResourceVisible(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setResourceVisible";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("Resource X", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("Resource Y", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("Visible ?", null, LuaScriptParameterType.BOOLEAN, false) };
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final int x = this.getParamInt(0);
            final int y = this.getParamInt(1);
            final boolean visible = this.getParamBool(2);
            final Resource res = ResourceManager.getInstance().getResource(x, y);
            if (res == null) {
                this.writeError(ResourceFunctionsLibrary.m_logger, "Aucune ressource trouv?e en [" + x + ";" + y + "]");
                return;
            }
            res.setVisible(visible);
        }
    }
    
    private static class IsResourceVisible extends JavaFunctionEx
    {
        IsResourceVisible(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "isResourceVisible";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("Resource X", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("Resource Y", null, LuaScriptParameterType.NUMBER, false) };
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("Visibility", null, LuaScriptParameterType.BOOLEAN, false) };
        }
        
        public void run(final int paramCount) throws LuaException {
            final int x = this.getParamInt(0);
            final int y = this.getParamInt(1);
            final Resource res = ResourceManager.getInstance().getResource(x, y);
            if (res == null) {
                this.writeError(ResourceFunctionsLibrary.m_logger, "Aucune ressource trouv?e en [" + x + ";" + y + "]");
                return;
            }
            this.addReturnValue(res.isVisible());
        }
    }
    
    private static class SetResourceAlpha extends JavaFunctionEx
    {
        SetResourceAlpha(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setResourceAlpha";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("Resource X", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("Resource Y", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("Alpha", null, LuaScriptParameterType.NUMBER, false) };
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        public void run(final int paramCount) throws LuaException {
            final int x = this.getParamInt(0);
            final int y = this.getParamInt(1);
            final float alpha = (float)this.getParamDouble(2);
            final Resource res = ResourceManager.getInstance().getResource(x, y);
            if (res == null) {
                this.writeError(ResourceFunctionsLibrary.m_logger, "Aucune ressource trouv?e en [" + x + ";" + y + "]");
                return;
            }
            res.setAlpha(alpha);
        }
    }
    
    private static class GetResourceAlpha extends JavaFunctionEx
    {
        GetResourceAlpha(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "getResourceAlpha";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("Resource X", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("Resource Y", null, LuaScriptParameterType.NUMBER, false) };
        }
        
        @Override
        public final LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("Alpha", null, LuaScriptParameterType.NUMBER, false) };
        }
        
        public void run(final int paramCount) throws LuaException {
            final int x = this.getParamInt(0);
            final int y = this.getParamInt(1);
            final Resource res = ResourceManager.getInstance().getResource(x, y);
            if (res == null) {
                this.writeError(ResourceFunctionsLibrary.m_logger, "Aucune ressource trouv?e en [" + x + ";" + y + "]");
                return;
            }
            this.addReturnValue(res.getAlpha());
        }
    }
    
    private static class AddResourceCreationCallback extends JavaFunctionEx
    {
        AddResourceCreationCallback(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "addResourceCreationCallback";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("Resource X", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("Resource Y", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("Function name", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("Parameters", null, LuaScriptParameterType.BLOOPS, true) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final LuaScript script = this.getScriptObject();
            final int x = this.getParamInt(0);
            final int y = this.getParamInt(1);
            final String function = this.getParamString(2);
            final LuaValue[] parameters = this.getParams(3, paramCount);
            if (ResourceManager.getInstance().getResource(x, y) != null) {
                script.runFunction(function, parameters, new LuaTable[0]);
            }
            ResourceManager.getInstance().addCreationListener(new ResourceCreationListener() {
                @Override
                public void onResourceCreation(final Resource resource) {
                    if (resource.getWorldCellX() == x && resource.getWorldCellY() == y) {
                        script.runFunction(function, parameters, new LuaTable[0]);
                        ResourceManager.getInstance().removeCreationListener(this);
                    }
                }
            });
        }
    }
    
    private static class AddResourceDestructionCallback extends JavaFunctionEx
    {
        AddResourceDestructionCallback(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "addResourceDestructionCallback";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("Resource X", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("Resource Y", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("Function name", null, LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("Parameters", null, LuaScriptParameterType.BLOOPS, true) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final LuaScript script = this.getScriptObject();
            final int x = this.getParamInt(0);
            final int y = this.getParamInt(1);
            final String function = this.getParamString(2);
            final LuaValue[] parameters = this.getParams(3, paramCount);
            if (ResourceManager.getInstance().getResource(x, y) != null) {
                script.runFunction(function, parameters, new LuaTable[0]);
            }
            ResourceManager.getInstance().addDestructionListener(new ResourceDestructionListener() {
                @Override
                public void onResourceDestruction(final Resource resource) {
                    if (resource.getWorldCellX() == x && resource.getWorldCellY() == y) {
                        script.runFunction(function, parameters, new LuaTable[0]);
                        ResourceManager.getInstance().removeDestructionListener(this);
                    }
                }
            });
        }
    }
}
