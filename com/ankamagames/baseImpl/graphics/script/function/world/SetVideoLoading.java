package com.ankamagames.baseImpl.graphics.script.function.world;

import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.alea.display.worldTransition.*;
import org.keplerproject.luajava.*;

public class SetVideoLoading extends JavaFunctionEx
{
    public SetVideoLoading(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setVideoLoading";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("videoName", null, LuaScriptParameterType.STRING, true), new LuaScriptParameterDescriptor("soundId", null, LuaScriptParameterType.INTEGER, true), new LuaScriptParameterDescriptor("fadeInDuration", null, LuaScriptParameterType.INTEGER, true), new LuaScriptParameterDescriptor("fadeOutDuration", null, LuaScriptParameterType.INTEGER, true) };
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final LoadingTransitionManager loading = LoadingTransitionManager.getInstance();
        if (paramCount == 0) {
            loading.reset();
            return;
        }
        final String videoName = (paramCount > 0) ? this.getParamString(0) : null;
        final int soundId = (paramCount > 1) ? this.getParamInt(1) : 0;
        final int fadeInDuration = (paramCount > 2) ? this.getParamInt(2) : 1000;
        final int fadeOutDuration = (paramCount > 3) ? this.getParamInt(3) : 1000;
        loading.setVideoName(videoName);
        loading.setSoundId(soundId);
        loading.setFadeInDuration(fadeInDuration);
        loading.setFadeOutDuration(fadeOutDuration);
    }
}
