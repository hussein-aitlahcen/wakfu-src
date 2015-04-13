package com.ankamagames.baseImpl.graphics.script.function.particle;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class SetRemoveWhenFar extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "setRemoveWhenFar";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULT;
    
    public SetRemoveWhenFar(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setRemoveWhenFar";
    }
    
    @Override
    public String getDescription() {
        return "si removeWhenFar=false, la particule ne sera pas supprim?e automatiquement quand on s'?loigne\n!!! ATTENTION!!! Penser ? la supprimer quand elle n'est plus utilis?e !!! ";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetRemoveWhenFar.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return SetRemoveWhenFar.RESULT;
    }
    
    public void run(final int paramCount) throws LuaException {
        final int id = this.getParamInt(0);
        final IsoParticleSystem system = IsoParticleSystemManager.getInstance().getParticleSystem(id);
        if (system == null) {
            this.writeError(SetRemoveWhenFar.m_logger, "pas de system d'id " + id);
            return;
        }
        system.setRemovedWhenFar(this.getParamBool(1));
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetRemoveWhenFar.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("id", "Id du systeme", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("removeWhenFar", "Supprime la particule quand elle est trop loin ?", LuaScriptParameterType.BOOLEAN, false) };
        RESULT = null;
    }
}
