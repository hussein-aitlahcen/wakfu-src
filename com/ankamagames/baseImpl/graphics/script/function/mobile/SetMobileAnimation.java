package com.ankamagames.baseImpl.graphics.script.function.mobile;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class SetMobileAnimation extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final String NAME = "setMobileAnimation";
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public SetMobileAnimation(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setMobileAnimation";
    }
    
    @Override
    public String getDescription() {
        return "joue une animation sur un mobile";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetMobileAnimation.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long mobileId = this.getParamLong(0);
        final String animation = this.getParamString(1);
        AnimatedElement mobile = MobileManager.getInstance().getMobile(mobileId);
        if (mobile == null) {
            mobile = SimpleAnimatedElementManager.getInstance().getAnimatedElement(mobileId);
        }
        if (mobile != null) {
            mobile.setAnimation(animation);
            mobile.forceReloadAnimation();
            if (paramCount > 2) {
                final LuaScript script = this.getScriptObject();
                final String func = this.getParamString(2);
                final LuaValue[] params = this.getParams(3, paramCount);
                final int taskId = script.registerWaitingTask(func, params);
                final AnimatedElement finalForListener = mobile;
                mobile.addAnimationEndedListener(new AnimationEndedListener() {
                    @Override
                    public void animationEnded(final AnimatedElement element) {
                        finalForListener.removeAnimationEndedListener(this);
                        script.executeWaitingTask(taskId);
                    }
                });
            }
        }
        else {
            this.writeError(SetMobileAnimation.m_logger, "le mobile " + mobileId + " n'existe pas ");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)SetMobileAnimation.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("mobileId", "Id du mobile", LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("animationName", "Nom de l'animation", LuaScriptParameterType.STRING, false), new LuaScriptParameterDescriptor("func", null, LuaScriptParameterType.STRING, true), new LuaScriptParameterDescriptor("params", null, LuaScriptParameterType.BLOOPS, true) };
    }
}
