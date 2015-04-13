package com.ankamagames.baseImpl.graphics.script.function.particle;

import com.ankamagames.baseImpl.graphics.isometric.tween.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class TimeTweenParticleSystem extends JavaFunctionEx
{
    private static final String NAME = "getTweenParticleSystemTime";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULT;
    
    public TimeTweenParticleSystem(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "getTweenParticleSystemTime";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return TimeTweenParticleSystem.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return TimeTweenParticleSystem.RESULT;
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
        final int particleFileId = this.getParamInt(0);
        final int startX = this.getParamInt(1);
        final int startY = this.getParamInt(2);
        final int startZ = this.getParamInt(3);
        final int destX = this.getParamInt(4);
        final int destY = this.getParamInt(5);
        final int destZ = this.getParamInt(6);
        final int angle = this.getParamInt(7);
        this.getParamInt(8);
        final float timeCoef = (paramCount >= 9) ? this.getParamFloat(9) : -1.0f;
        FreeParticleSystem system;
        if (paramCount >= 10) {
            system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(particleFileId, this.getParamInt(10));
        }
        else {
            system = IsoParticleSystemFactory.getInstance().getFreeParticleSystem(particleFileId);
        }
        system.setPosition(startX, startY, startZ);
        system.setFightId(fightId);
        ParabolicTween tween;
        if (timeCoef < 0.0f) {
            tween = new ParabolicTween(system, destX, destY, destZ, angle);
        }
        else {
            tween = new ParabolicTween(system, destX, destY, destZ, angle, timeCoef);
        }
        this.addReturnValue((int)tween.getTweenDuration());
        system.removeReference();
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("particleFileId", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("startX", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("startY", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("startZ", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("destX", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("destY", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("destZ", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("angle", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("type", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("timeCoef", null, LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("level", null, LuaScriptParameterType.INTEGER, true) };
        RESULT = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("movementDuration", null, LuaScriptParameterType.INTEGER, false) };
    }
}
