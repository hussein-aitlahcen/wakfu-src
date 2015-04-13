package com.ankamagames.baseImpl.graphics.script;

import com.ankamagames.framework.script.*;
import org.keplerproject.luajava.*;

public class ResetMusicMix extends JavaFunctionEx
{
    public ResetMusicMix(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "resetMusicMix";
    }
    
    @Override
    public String getDescription() {
        return "Ram?ne le r?glage du mixage musique/son de combat aux param?tres par d?faut";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return null;
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    @Override
    protected void run(final int paramCount) throws LuaException {
        SoundFunctionsLibrary.getInstance().resetLinkerMix();
    }
}
