package com.ankamagames.baseImpl.graphics.sound;

import com.ankamagames.framework.sound.*;
import gnu.trove.*;
import com.ankamagames.framework.sound.util.*;
import org.apache.commons.lang3.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.framework.sound.openAL.*;

public abstract class BaseParticleSoundManager extends ParticleSoundManager
{
    private final TIntLongHashMap m_soundMap;
    private final TLongLongHashMap m_lastPlayedSoundTime;
    private static final int TIME_BEFORE_NEXT_PLAY = 700;
    private final long[] PARTICLE_PREFIXES_TO_CHECK;
    
    public BaseParticleSoundManager() {
        super();
        this.m_soundMap = new TIntLongHashMap();
        this.m_lastPlayedSoundTime = new TLongLongHashMap();
        this.PARTICLE_PREFIXES_TO_CHECK = new long[] { 400L, 410L, 420L };
    }
    
    @Override
    public void registerSound(final int particleUID, final long soundUID, final long time, final long fileId) {
        this.m_soundMap.put(particleUID, soundUID);
        this.m_lastPlayedSoundTime.put(fileId, time);
    }
    
    private long unregisterSound(final int particleUID) {
        return this.m_soundMap.remove(particleUID);
    }
    
    @Override
    public boolean canPlaySoundById(final long currentTime, final long soundId) {
        final long prefix = SoundGroupUtils.getSoundPrefix(soundId);
        if (!ArrayUtils.contains(this.PARTICLE_PREFIXES_TO_CHECK, prefix)) {
            return true;
        }
        final long lastTime = this.m_lastPlayedSoundTime.get(soundId);
        return 700L <= Math.abs(currentTime - lastTime);
    }
    
    protected boolean canPlaySound() {
        return SoundFunctionsLibrary.getInstance().canPlaySound();
    }
    
    @Override
    public void cleanAps(final int apsId, final boolean andStopSound) {
        final long soundUID = this.unregisterSound(apsId);
        if (andStopSound) {
            final AudioSource source = AudioSourceManager.getInstance().getAudioSource(soundUID);
            SoundFunctionsLibrary.getInstance().stopSound(soundUID, source);
        }
    }
}
