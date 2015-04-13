package com.ankamagames.baseImpl.graphics.script.function.particle;

import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class RemoveParticleSystem extends JavaFunctionEx
{
    private static final String NAME = "removeParticleSystem";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public RemoveParticleSystem(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "removeParticleSystem";
    }
    
    @Override
    public String getDescription() {
        return "D?truit un systeme de particule existant";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return RemoveParticleSystem.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final int particleUniqueId = this.getParamInt(0);
        final boolean stopAndkill = paramCount >= 2 && this.getParamBool(1);
        IsoParticleSystemManager.getInstance().removeParticleSystem(particleUniqueId, stopAndkill);
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("particleUniqueId", "L'Id unique de l'APS", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("stopEmittersToKill", "Si true, les emetteurs seront coup?s et le syst?me ne sera supprim? que lorsqu'il n'y a plus de particules vivantes. Si false, d?truit imm?diatement.", LuaScriptParameterType.BOOLEAN, true) };
    }
}
