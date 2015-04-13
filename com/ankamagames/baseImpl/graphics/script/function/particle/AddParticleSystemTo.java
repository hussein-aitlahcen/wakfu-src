package com.ankamagames.baseImpl.graphics.script.function.particle;

import org.apache.log4j.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import org.keplerproject.luajava.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;

public abstract class AddParticleSystemTo extends JavaFunctionEx
{
    private static final Logger m_logger;
    
    protected AddParticleSystemTo(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("systemId", null, LuaScriptParameterType.INTEGER, false) };
    }
    
    protected abstract AnimatedElement getTarget(final long p0);
    
    protected abstract void setTarget(final FreeParticleSystem p0, final AnimatedElement p1, final int p2) throws LuaException;
    
    protected final void setInFight(final FreeParticleSystem system) throws LuaException {
        final int fightId = this.getScriptObject().getFightId();
        if (fightId != -1) {
            system.setFightId(fightId);
        }
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final long targetId = this.getParamLong(1);
        final AnimatedElement target = this.getTarget(targetId);
        if (target != null && !IsoParticleSystemFactory.getInstance().canCreateParticleForFight(target.getCurrentFightId())) {
            this.addReturnValue(0);
            return;
        }
        final int particleId = this.getParamInt(0);
        FreeParticleSystem system;
        if (paramCount >= 3) {
            system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(particleId, this.getParamInt(2));
        }
        else {
            system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(particleId);
        }
        if (target != null) {
            this.setTarget(system, target, paramCount);
            IsoParticleSystemManager.getInstance().addParticleSystem(system);
            this.addReturnValue(system.getId());
        }
        else {
            this.writeError(AddParticleSystemTo.m_logger, "pas d'?l?ment interactif trouv? " + targetId);
            this.addReturnNilValue();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)AddParticleSystemTo.class);
    }
}
