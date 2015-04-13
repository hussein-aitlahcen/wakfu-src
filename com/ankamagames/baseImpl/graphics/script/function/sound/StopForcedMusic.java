package com.ankamagames.baseImpl.graphics.script.function.sound;

import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.script.*;
import org.keplerproject.luajava.*;

public class StopForcedMusic extends JavaFunctionEx
{
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public StopForcedMusic(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "stopForcedMusic";
    }
    
    @Override
    public String getDescription() {
        return "Ne force plus la musique courante";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return StopForcedMusic.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        SoundFunctionsLibrary.getInstance().stopForcedMusic();
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[0];
    }
}
