package com.ankamagames.baseImpl.graphics.script.function.sound;

import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class PlayMusic extends JavaFunctionEx
{
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public PlayMusic(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public String getName() {
        return "playMusic";
    }
    
    @Override
    public String getDescription() {
        return "Joue une musique (son relativement long, et bouclant) au sein du monde.";
    }
    
    @Override
    public LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return PlayMusic.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public void run(final int paramCount) throws LuaException {
        final long id = this.getParamLong(0);
    }
    
    static {
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("musicFileId", "Id du fichier ? jouer", LuaScriptParameterType.LONG, false) };
    }
}
