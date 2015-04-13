package com.ankamagames.baseImpl.graphics.script;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.framework.graphics.engine.light.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;

public class LightFunctionsLibrary extends JavaFunctionsLibrary
{
    private static final Logger m_logger;
    private static final boolean DEBUG = true;
    private static final LightFunctionsLibrary m_instance;
    private static int ID;
    private final TIntObjectHashMap<IsoLightSource> m_addedLightSpot;
    private final TIntObjectHashMap<ScriptedLightModifier> m_addedGlobalLight;
    
    @Override
    public final String getName() {
        return "Light";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    private static ScriptedLightModifier getAddedGlobalLight(final int id) {
        return LightFunctionsLibrary.m_instance.m_addedGlobalLight.get(id);
    }
    
    private static void removeGlobalLight(final int id) {
        LightFunctionsLibrary.m_instance.m_addedGlobalLight.remove(id);
    }
    
    private static void putGlobalLight(final int id, final ScriptedLightModifier lightModifier) {
        LightFunctionsLibrary.m_instance.m_addedGlobalLight.put(id, lightModifier);
    }
    
    private static IsoLightSource getAddedLightSpot(final int id) {
        return LightFunctionsLibrary.m_instance.m_addedLightSpot.get(id);
    }
    
    private static void removeLightSpot(final int id) {
        LightFunctionsLibrary.m_instance.m_addedLightSpot.remove(id);
    }
    
    private static void putLightSpot(final int id, final IsoLightSource lightModifier) {
        LightFunctionsLibrary.m_instance.m_addedLightSpot.put(id, lightModifier);
    }
    
    public static LightFunctionsLibrary getInstance() {
        return LightFunctionsLibrary.m_instance;
    }
    
    private LightFunctionsLibrary() {
        super();
        this.m_addedLightSpot = new TIntObjectHashMap<IsoLightSource>();
        this.m_addedGlobalLight = new TIntObjectHashMap<ScriptedLightModifier>();
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new AddPointLight(luaState), new RemovePointLight(luaState), new SetPointLightPosition(luaState), new SetPointLightColor(luaState), new SetPointLightRange(luaState), new AddGlobalLight(luaState), new RemoveGlobalLight(luaState), new SetGlobalLightColor(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)LightFunctionsLibrary.class);
        m_instance = new LightFunctionsLibrary();
        LightFunctionsLibrary.ID = 0;
    }
    
    private static class AddPointLight extends JavaFunctionEx
    {
        AddPointLight(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "addPointLight";
        }
        
        @Override
        public String getDescription() {
            return "Ajoute une lumiere ponctuelle";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("posX", "Position x", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("posY", "Position y", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("posZ", "Position z", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("red", "Teinte de rouge", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("green", "Teinte de vert", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("blue", "Teinte de bleu", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("radius", "Rayon de lumi?re", LuaScriptParameterType.NUMBER, true) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("lightId", "Id de la lumi?re", LuaScriptParameterType.INTEGER, false) };
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final IsoLightSource lightSpot = (IsoLightSource)LightSourceManagerDelegate.INSTANCE.createLightSource();
            lightSpot.setTarget(new DefaultIsoWorldTarget(this.getParamFloat(0), this.getParamFloat(1), this.getParamFloat(2)));
            lightSpot.setSaturation(this.getParamFloat(3), this.getParamFloat(4), this.getParamFloat(5));
            if (paramCount == 7) {
                lightSpot.setRange(this.getParamFloat(6));
            }
            IsoSceneLightManager.INSTANCE.addLight(lightSpot);
            final int id = ++LightFunctionsLibrary.ID;
            putLightSpot(id, lightSpot);
            this.addReturnValue(id);
        }
    }
    
    private static class RemovePointLight extends JavaFunctionEx
    {
        RemovePointLight(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "removePointLight";
        }
        
        @Override
        public String getDescription() {
            return "Supprime une lumi?re ponctuelle";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("lightId", "Id de la lumi?re", LuaScriptParameterType.INTEGER, false) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final int id = this.getParamInt(0);
            IsoSceneLightManager.INSTANCE.removeLight(getAddedLightSpot(id));
            removeLightSpot(id);
        }
    }
    
    private static class SetPointLightColor extends JavaFunctionEx
    {
        SetPointLightColor(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setPointLightColor";
        }
        
        @Override
        public String getDescription() {
            return "Change la couleur d'une lumi?re ponctuelle";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("lightId", "Id de la lumi?re", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("red", "Teinte de rouge", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("green", "Teinte de vert", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("blue", "Teinte de bleu", LuaScriptParameterType.NUMBER, false) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final IsoLightSource lightSpot = getAddedLightSpot(this.getParamInt(0));
            if (lightSpot != null) {
                lightSpot.modifySpecular(this.getParamFloat(1), this.getParamFloat(2), this.getParamFloat(3));
            }
            else {
                this.writeError(LightFunctionsLibrary.m_logger, "La lumi?re n'existe pas");
            }
        }
    }
    
    private static class SetPointLightPosition extends JavaFunctionEx
    {
        SetPointLightPosition(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setPointLightPosition";
        }
        
        @Override
        public String getDescription() {
            return "Repositionne une lumi?re pmonctuelle";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("lightId", "Id de lumi?re", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("x", "Position x", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("y", "Position y", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("z", "Position z", LuaScriptParameterType.NUMBER, false) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final IsoLightSource lightSpot = getAddedLightSpot(this.getParamInt(0));
            if (lightSpot != null) {
                final Vector3 pos = lightSpot.getPosition();
                pos.set(this.getParamFloat(1), this.getParamFloat(2), this.getParamFloat(3));
            }
            else {
                this.writeError(LightFunctionsLibrary.m_logger, "La lumi?re n'existe pas");
            }
        }
    }
    
    private static class SetPointLightRange extends JavaFunctionEx
    {
        SetPointLightRange(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setPointLightRange";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("lightId", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("range", null, LuaScriptParameterType.NUMBER, false) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final IsoLightSource lightSpot = getAddedLightSpot(this.getParamInt(0));
            if (lightSpot != null) {
                lightSpot.setRange(this.getParamFloat(1));
            }
            else {
                this.writeError(LightFunctionsLibrary.m_logger, "La lumi?re n'existe pas");
            }
        }
    }
    
    private static class AddGlobalLight extends JavaFunctionEx
    {
        AddGlobalLight(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "addGlobalLight";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("red", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("green", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("blue", null, LuaScriptParameterType.NUMBER, false) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("lightId", null, LuaScriptParameterType.INTEGER, false) };
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final ScriptedLightModifier lightModifier = new ScriptedLightModifier();
            lightModifier.setColor(this.getParamFloat(0), this.getParamFloat(1), this.getParamFloat(2));
            IsoSceneLightManager.INSTANCE.addLightingModifier(lightModifier);
            final int id = ++LightFunctionsLibrary.ID;
            putGlobalLight(id, lightModifier);
            this.addReturnValue(id);
        }
    }
    
    private static class RemoveGlobalLight extends JavaFunctionEx
    {
        RemoveGlobalLight(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "removeGlobalLight";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("lightId", null, LuaScriptParameterType.INTEGER, false) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final int id = this.getParamInt(0);
            IsoSceneLightManager.INSTANCE.removeLightingModifier(getAddedGlobalLight(id));
            removeGlobalLight(id);
        }
    }
    
    private static class SetGlobalLightColor extends JavaFunctionEx
    {
        SetGlobalLightColor(final LuaState luaState) {
            super(luaState);
        }
        
        @Override
        public String getName() {
            return "setGlobalLightColor";
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getParameterDescriptors() {
            return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("lightId", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("red", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("green", null, LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("blue", null, LuaScriptParameterType.NUMBER, false) };
        }
        
        @Override
        public LuaScriptParameterDescriptor[] getResultDescriptors() {
            return null;
        }
        
        @Override
        protected void run(final int paramCount) throws LuaException {
            final ScriptedLightModifier lightModifier = getAddedGlobalLight(this.getParamInt(0));
            if (lightModifier != null) {
                lightModifier.setColor(this.getParamFloat(1), this.getParamFloat(2), this.getParamFloat(3));
            }
            else {
                this.writeError(LightFunctionsLibrary.m_logger, "La lumi?re n'existe pas");
            }
        }
    }
}
