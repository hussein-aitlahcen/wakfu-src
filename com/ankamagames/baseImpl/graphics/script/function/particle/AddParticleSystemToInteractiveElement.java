package com.ankamagames.baseImpl.graphics.script.function.particle;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class AddParticleSystemToInteractiveElement extends AddParticleSystemTo
{
    private static final Logger m_logger;
    private static final String NAME = "addParticleSystemToIe";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public AddParticleSystemToInteractiveElement(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "addParticleSystemToIe";
    }
    
    @Override
    public String getDescription() {
        return "Ajoute un systeme de particule ? un element interactif";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return AddParticleSystemToInteractiveElement.PARAMS;
    }
    
    @Override
    protected AnimatedElement getTarget(final long targetId) {
        return AnimatedElementSceneViewManager.getInstance().getElement(targetId);
    }
    
    @Override
    protected void setTarget(final FreeParticleSystem system, final AnimatedElement target, final int paramCount) throws LuaException {
        system.setTarget(target);
    }
    
    static {
        m_logger = Logger.getLogger((Class)AddParticleSystemToInteractiveElement.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("particleFileId", "Id du systeme", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("targetId", "Id de l'element interactif", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("level", "Niveau du systeme", LuaScriptParameterType.INTEGER, true) };
    }
}
