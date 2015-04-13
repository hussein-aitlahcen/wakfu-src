package com.ankamagames.baseImpl.graphics.script.function.sound;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class PlayRandomSound extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public PlayRandomSound(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public final String getName() {
        return "playRandomSound";
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return PlayRandomSound.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public final void run(final int paramCount) throws LuaException {
        if (!SoundFunctionsLibrary.getInstance().canPlaySound()) {
            return;
        }
        if (paramCount % 2 != 0) {
            return;
        }
        final int choice = MathHelper.random(0, paramCount / 2);
        final long soundId = this.getParamLong(2 * choice);
        final int gainMod = this.getParamInt(2 * choice + 1);
        try {
            if (soundId != 0L) {
                SoundFunctionsLibrary.getInstance().playSound(soundId, gainMod / 100.0f, 1, -1L, -1L, this.getScriptObject().getFightId());
            }
            else {
                PlayRandomSound.m_logger.error((Object)"Id du son nul");
            }
        }
        catch (Exception e) {
            this.writeError(PlayRandomSound.m_logger, "soundExtension or soundPath not initialized " + e);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)PlaySound.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("soundId, gain", null, LuaScriptParameterType.BLOOPS, true) };
    }
}
