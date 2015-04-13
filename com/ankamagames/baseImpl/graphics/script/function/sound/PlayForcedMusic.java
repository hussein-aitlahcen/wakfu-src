package com.ankamagames.baseImpl.graphics.script.function.sound;

import com.ankamagames.baseImpl.graphics.script.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class PlayForcedMusic extends JavaFunctionEx
{
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public PlayForcedMusic(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "playForcedMusic";
    }
    
    @Override
    public String getDescription() {
        return "Force la musique courante";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return PlayForcedMusic.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long id = this.getParamLong(0);
        SoundFunctionsLibrary.getInstance().playForcedMusic(id);
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("musicFileId", "Id du fichier \u00e0 jouer", LuaScriptParameterType.LONG, false) };
    }
}
