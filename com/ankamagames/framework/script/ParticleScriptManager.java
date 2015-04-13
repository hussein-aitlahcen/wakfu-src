package com.ankamagames.framework.script;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;

public final class ParticleScriptManager
{
    private static Logger m_scriptsLogger;
    private static ParticleScriptManager m_instance;
    private static final String PARTICLES_PATH = "particles/";
    
    public static ParticleScriptManager getInstance() {
        return ParticleScriptManager.m_instance;
    }
    
    public String createParticleFileName(final int particleId) {
        assert LuaManager.getInstance().getPath() != null;
        return String.format("%s%d%s", "particles/", particleId, LuaManager.getInstance().getExtension());
    }
    
    @Nullable
    private LuaScript getScript(final int particleId) {
        return LuaManager.getInstance().loadScript(this.createParticleFileName(particleId), null, true);
    }
    
    public boolean playParticleSound(final int particleId, final int[] params) {
        final LuaValue[] returnValues = this.runScript(particleId, params, "playAps");
        if (returnValues == null) {
            return false;
        }
        if (returnValues.length == 0) {
            return false;
        }
        final LuaValue value = returnValues[0];
        return value.getType() == LuaScriptParameterType.BOOLEAN && (boolean)returnValues[0].getValue();
    }
    
    private LuaValue[] runScript(final int particleId, final int[] params, final String funcName) {
        final LuaScript s = this.getScript(particleId);
        if (s == null) {
            return null;
        }
        final LuaValue[] typedParams = new LuaValue[params.length];
        for (int i = 0; i < typedParams.length; ++i) {
            typedParams[i] = new LuaValue(params[i]);
        }
        final LuaValue[] returnValue = s.runFunction(funcName, typedParams, new LuaTable[0]);
        s.tryToFinish();
        return returnValue;
    }
    
    public void clear() {
    }
    
    static {
        ParticleScriptManager.m_scriptsLogger = Logger.getLogger("ParticleScripts");
        ParticleScriptManager.m_instance = new ParticleScriptManager();
    }
}
