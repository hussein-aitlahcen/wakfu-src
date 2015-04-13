package com.ankamagames.baseImpl.graphics.script.function.sound;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.framework.sound.openAL.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class StopSound extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public StopSound(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public final String getName() {
        return "stopSound";
    }
    
    @Override
    public String getDescription() {
        return "Stoppe un son au sein du monde.";
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return StopSound.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public final void run(final int paramCount) throws LuaException {
        final long soundFileId = this.getParamLong(0);
        final AudioSource source = AudioSourceManager.getInstance().getAudioSource(soundFileId);
        SoundFunctionsLibrary.getInstance().stopSound(soundFileId, source);
    }
    
    static {
        m_logger = Logger.getLogger((Class)StopSound.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("soundUID", "UID du fichier son", LuaScriptParameterType.LONG, false) };
    }
}
