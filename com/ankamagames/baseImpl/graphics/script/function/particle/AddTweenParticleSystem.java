package com.ankamagames.baseImpl.graphics.script.function.particle;

import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.isometric.tween.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class AddTweenParticleSystem extends JavaFunctionEx
{
    private static final String NAME = "addTweenParticleSystem";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    private static final LuaScriptParameterDescriptor[] RESULT;
    
    public AddTweenParticleSystem(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "addTweenParticleSystem";
    }
    
    @Override
    public String getDescription() {
        return "Ajoute un syst?me de particules en le faisant se d?placer entre deux points du monde. Attention, plus l'angle est faible plus le chemin est court, et donc plus le syst?me aura tendance ? aller vite.";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return AddTweenParticleSystem.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return AddTweenParticleSystem.RESULT;
    }
    
    public void run(final int paramCount) throws LuaException {
        final LuaObject luaObject = this.getScriptObject().getLuaState().getLuaObject("fightId");
        int fightId = -1;
        if (luaObject.isNumber()) {
            fightId = (int)luaObject.getNumber();
            if (!IsoParticleSystemFactory.getInstance().canCreateParticleForFight(fightId)) {
                this.addReturnValue(0);
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
        ParabolicTween tween;
        if (timeCoef < 0.0f) {
            tween = new ParabolicTween(system, destX, destY, destZ, angle);
        }
        else {
            tween = new ParabolicTween(system, destX, destY, destZ, angle, timeCoef);
        }
        final int tweenDuration = (int)tween.getTweenDuration();
        system.setFightId(fightId);
        IsoParticleSystemManager.getInstance().addParticleSystem(system);
        TweenManager.getInstance().addTween(tween);
        this.addReturnValue(system.getId());
        this.addReturnValue(tweenDuration);
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("particleFileId", "Id du systeme", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("startX", "Position de depart x", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("startY", "Position de depart y", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("startZ", "Position de depart z", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("destX", "Position de destination x", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("destY", "Position de destination y", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("destZ", "Position de destination z", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("angle", "Angle de la courbure que le syst?me suivra pour aller d'un point ? l'autre", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("type", "Inutilis? (laisser ? 1)", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("timeCoef", "Valeur permettant d'accelerer ou de ralentir le d?placement", LuaScriptParameterType.NUMBER, true), new LuaScriptParameterDescriptor("level", "Niveau du systeme", LuaScriptParameterType.INTEGER, true) };
        RESULT = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("systemId", "Id unique du systeme", LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("movementDuration", "Temps en ms que mettra le d?placement", LuaScriptParameterType.INTEGER, false) };
    }
}
