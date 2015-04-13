package com.ankamagames.baseImpl.graphics.script.function.sound;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.framework.sound.*;
import com.ankamagames.framework.sound.group.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.framework.sound.openAL.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class PlayApsSound extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public PlayApsSound(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public final String getName() {
        return "playApsSound";
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return PlayApsSound.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public final void run(final int paramCount) throws LuaException {
        if (!SoundFunctionsLibrary.getInstance().canPlaySound()) {
            return;
        }
        final long date = System.currentTimeMillis();
        final long soundFileId = this.getParamLong(0);
        if (!ParticleSoundManager.getInstance().canPlaySoundById(date, soundFileId)) {
            return;
        }
        final int fightId = this.getParamInt(1);
        final float gain = this.getParamInt(2) / 100.0f;
        final int apsId = this.getParamInt(3);
        final int duration = this.getParamInt(4);
        final int fadeOutTime = this.getParamInt(5);
        final int rollOffId = this.getParamInt(6);
        final boolean loop = this.getParamBool(7);
        final long endDate = (duration != -1) ? (date + duration) : -1L;
        final long fadeDate = (fadeOutTime != 0) ? (endDate - fadeOutTime) : -1L;
        final IsoParticleSystem ps = IsoParticleSystemManager.getInstance().getParticleSystem(apsId);
        try {
            if (soundFileId != 0L) {
                AudioSourceDefinition source;
                if (ps != null) {
                    source = SoundFunctionsLibrary.getInstance().playSound(soundFileId, gain, loop ? 0 : 1, endDate, fadeDate, fightId, ps, rollOffId, false);
                }
                else {
                    source = SoundFunctionsLibrary.getInstance().playSound(soundFileId, gain, loop ? 0 : 1, endDate, fadeDate, fightId);
                }
                if (source != null) {
                    ParticleSoundManager.getInstance().registerSound(apsId, source.getSoundUID(), date, soundFileId);
                }
            }
            else {
                PlayApsSound.m_logger.error((Object)"Id du son nul");
            }
        }
        catch (Exception e) {
            this.writeError(PlayApsSound.m_logger, "soundExtension or soundPath not initialized " + e);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)PlayApsSound.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("soundFileId", null, LuaScriptParameterType.LONG, false), new LuaScriptParameterDescriptor("fightId", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("gain", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("apsId", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("duration", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("fadeOutTime", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("rollOffId", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("loop", null, LuaScriptParameterType.BOOLEAN, false) };
    }
}
