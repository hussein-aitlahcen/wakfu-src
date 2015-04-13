package com.ankamagames.baseImpl.graphics.script.function.particle;

import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class AddParticleSystem extends JavaFunctionEx
{
    private static final String NAME = "addParticleSystem";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULT;
    
    public AddParticleSystem(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "addParticleSystem";
    }
    
    @Override
    public String getDescription() {
        return "Ajoute un syst?me de particules ? un endroit bien pr?cis du monde.";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return AddParticleSystem.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return AddParticleSystem.RESULT;
    }
    
    public void run(final int paramCount) throws LuaException {
        final LuaObject luaObject = this.getScriptObject().getLuaState().getLuaObject("fightId");
        int fightId = -1;
        if (luaObject.isNumber()) {
            fightId = (int)luaObject.getNumber();
            if (!IsoParticleSystemFactory.getInstance().canCreateParticleForFight(fightId)) {
                this.addReturnValue(0);
                return;
            }
        }
        final int particleId = this.getParamInt(0);
        if (particleId == 0) {
            this.addReturnValue(-1);
            return;
        }
        if (paramCount >= 6) {
            fightId = this.getParamInt(5);
        }
        final int worldX = this.getParamInt(1);
        final int worldY = this.getParamInt(2);
        final int altitude = this.getParamInt(3);
        FreeParticleSystem system;
        if (paramCount >= 5) {
            system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(particleId, this.getParamInt(4));
        }
        else {
            system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(particleId);
        }
        system.setPosition(worldX, worldY, altitude);
        system.setFightId(fightId);
        IsoParticleSystemManager.getInstance().addParticleSystem(system);
        this.addReturnValue(system.getId());
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("particleFileId", "Id du systeme", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("x", "Position x", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("y", "Position y", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("z", "Position z", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("level", "Niveau du systeme", LuaScriptParameterType.INTEGER, true), new LuaScriptParameterDescriptor("fightId", null, LuaScriptParameterType.INTEGER, true) };
        RESULT = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("systemId", "Id unique de l'APS", LuaScriptParameterType.INTEGER, false) };
    }
}
