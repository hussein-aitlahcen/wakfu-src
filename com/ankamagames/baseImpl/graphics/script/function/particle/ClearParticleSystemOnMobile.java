package com.ankamagames.baseImpl.graphics.script.function.particle;

import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class ClearParticleSystemOnMobile extends JavaFunctionEx
{
    private static final String NAME = "clearParticleSystemOnMobile";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public ClearParticleSystemOnMobile(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "clearParticleSystemOnMobile";
    }
    
    @Override
    public String getDescription() {
        return "D?truit les syst?mes de particules attach?s ? un mobile";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return ClearParticleSystemOnMobile.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final Mobile mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile == null) {
            return;
        }
        IsoParticleSystemManager.getInstance().clearParticlesOnTarget(mobile);
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "L'Id unique du mobile", LuaScriptParameterType.LONG, false) };
    }
}
