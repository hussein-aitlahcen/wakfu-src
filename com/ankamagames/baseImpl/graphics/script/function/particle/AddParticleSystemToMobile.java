package com.ankamagames.baseImpl.graphics.script.function.particle;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class AddParticleSystemToMobile extends AddParticleSystemTo
{
    private static final Logger m_logger;
    private static final String NAME = "addParticleSystemToTarget";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public AddParticleSystemToMobile(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "addParticleSystemToTarget";
    }
    
    @Override
    public String getDescription() {
        return "Ajoute un systeme de particule ? un mobile";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return AddParticleSystemToMobile.PARAMS;
    }
    
    @Override
    protected AnimatedElement getTarget(final long targetId) {
        final Mobile mobile = MobileManager.getInstance().getMobile(targetId);
        if (mobile != null) {
            return mobile;
        }
        return SimpleAnimatedElementManager.getInstance().getAnimatedElement(targetId);
    }
    
    @Override
    protected void setTarget(final FreeParticleSystem system, final AnimatedElement target, final int paramCount) throws LuaException {
        system.setTarget(target);
        this.setInFight(system);
    }
    
    static {
        m_logger = Logger.getLogger((Class)AddParticleSystemToMobile.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("particleFileId", "Id du systeme", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("targetId", "Id du mobile", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("level", "Niveau du systeme", LuaScriptParameterType.INTEGER, true) };
    }
}
