package com.ankamagames.baseImpl.graphics.script;

import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class SetMusicMix extends JavaFunctionEx
{
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public SetMusicMix(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "setMusicMix";
    }
    
    @Override
    public String getDescription() {
        return "R?gle le volume auquel mettre la musique lorsqu'on joue les sons de combat, et le temps de transition vers ce volume";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return SetMusicMix.PARAMS;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        final float targetGain = (float)this.getParamDouble(0);
        float fadeOutTime = -1.0f;
        if (paramCount > 1) {
            fadeOutTime = (float)this.getParamDouble(1);
        }
        SoundFunctionsLibrary.getInstance().setLinkerMix(targetGain, fadeOutTime);
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("targetGain", "Volume entre 0 et 100", LuaScriptParameterType.NUMBER, false), new LuaScriptParameterDescriptor("fadeOutTime", "Temps de transition", LuaScriptParameterType.NUMBER, true) };
    }
}
