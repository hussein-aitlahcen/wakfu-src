package com.ankamagames.baseImpl.graphics.script.function.world;

import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.alea.display.worldTransition.*;
import org.keplerproject.luajava.*;

public class SetLoading extends JavaFunctionEx
{
    public SetLoading(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setLoading";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("anmName", null, LuaScriptParameterType.STRING, true), new LuaScriptParameterDescriptor("durationMin", null, LuaScriptParameterType.INTEGER, true), new LuaScriptParameterDescriptor("soundId", null, LuaScriptParameterType.INTEGER, true), new LuaScriptParameterDescriptor("fadeInDuration", null, LuaScriptParameterType.INTEGER, true), new LuaScriptParameterDescriptor("fadeOutDuration", null, LuaScriptParameterType.INTEGER, true) };
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
        final String anmName = (paramCount > 0) ? this.getParamString(0) : null;
        final int durationMin = (paramCount > 1) ? this.getParamInt(1) : 0;
        final int soundId = (paramCount > 2) ? this.getParamInt(2) : 0;
        final int fadeInDuration = (paramCount > 3) ? this.getParamInt(3) : 1000;
        final int fadeOutDuration = (paramCount > 4) ? this.getParamInt(4) : 1000;
        loading.setAnmName(anmName);
        loading.setMinTransitionDuration(durationMin);
        loading.setSoundId(soundId);
        loading.setFadeInDuration(fadeInDuration);
        loading.setFadeOutDuration(fadeOutDuration);
    }
}
