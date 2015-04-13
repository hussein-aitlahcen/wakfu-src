package com.ankamagames.baseImpl.graphics.script.function.particle;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class AddParticleSystemToMobileWithOffset extends AddParticleSystemToMobile
{
    private static final Logger m_logger;
    private static final String NAME = "addParticleSystemToTargetWithOffset";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public AddParticleSystemToMobileWithOffset(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "addParticleSystemToTargetWithOffset";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return AddParticleSystemToMobileWithOffset.PARAMS;
    }
    
    @Override
    protected void setTarget(final FreeParticleSystem system, final AnimatedElement target, final int paramCount) throws LuaException {
        final int offsetZ = (paramCount == 4) ? this.getParamInt(3) : target.getVisualHeight();
        system.setTarget(target, 0.0f, offsetZ);
        this.setInFight(system);
    }
    
    static {
        m_logger = Logger.getLogger((Class)AddParticleSystemToMobileWithOffset.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("particleFileId", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("targetId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("level", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("height", null, LuaScriptParameterType.INTEGER, true) };
    }
}
