package com.ankamagames.baseImpl.graphics.script.function.sound;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.sound.*;
import com.ankamagames.framework.sound.group.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.framework.sound.openAL.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;

public class PlayRandomApsSound extends JavaFunctionEx
{
    private static final Logger m_logger;
    private static final LuaScriptParameterDescriptor[] PARAMS;
    
    public PlayRandomApsSound(final LuaState luaState) {
        super(luaState);
    }
    
    @Override
    public final String getName() {
        return "playRandomApsSound";
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getParameterDescriptors() {
        return PlayRandomApsSound.PARAMS;
    }
    
    @Override
    public final LuaScriptParameterDescriptor[] getResultDescriptors() {
        return null;
    }
    
    public final void run(final int paramCount) throws LuaException {
        if (!SoundFunctionsLibrary.getInstance().canPlaySound()) {
            return;
        }
        final int fightId = this.getParamInt(0);
        final int apsId = this.getParamInt(1);
        final int duration = this.getParamInt(2);
        final int fadeOutTime = this.getParamInt(3);
        final int rollOffId = this.getParamInt(4);
        final boolean loop = this.getParamBool(5);
        final int soundIndex = MathHelper.random((paramCount - 6) / 2) * 2 + 6;
        final long soundFileId = this.getParamLong(soundIndex);
        final long date = System.currentTimeMillis();
        if (!ParticleSoundManager.getInstance().canPlaySoundById(date, soundFileId)) {
            return;
        }
        final float gain = this.getParamInt(soundIndex + 1) / 100.0f;
        final long endDate = date + duration;
        final long fadeDate = (fadeOutTime != 0) ? (endDate - fadeOutTime) : 0L;
        final IsoParticleSystem ps = IsoParticleSystemManager.getInstance().getParticleSystem(apsId);
        try {
            if (soundFileId != 0L) {
                AudioSourceDefinition source;
                if (ps == null) {
                    source = SoundFunctionsLibrary.getInstance().playSound(soundFileId, gain, loop ? 0 : 1, endDate, fadeDate, fightId);
                }
                else {
                    source = SoundFunctionsLibrary.getInstance().playSound(soundFileId, gain, loop ? 0 : 1, endDate, fadeDate, fightId, ps, rollOffId, false);
                }
                if (source != null) {
                    ParticleSoundManager.getInstance().registerSound(apsId, source.getSoundUID(), date, soundFileId);
                }
            }
            else {
                PlayRandomApsSound.m_logger.error((Object)"Id du son nul");
            }
        }
        catch (Exception e) {
            this.writeError(PlayRandomApsSound.m_logger, "soundExtension or soundPath not initialized " + e);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)PlayRandomApsSound.class);
        PARAMS = new LuaScriptParameterDescriptor[] { new LuaScriptParameterDescriptor("fightId", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("apsId", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("duration", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("fadeOutTime", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("rollOffId", null, LuaScriptParameterType.INTEGER, false), new LuaScriptParameterDescriptor("loop", null, LuaScriptParameterType.BOOLEAN, false), new LuaScriptParameterDescriptor("soundId,gain", null, LuaScriptParameterType.BLOOPS, true) };
    }
}
