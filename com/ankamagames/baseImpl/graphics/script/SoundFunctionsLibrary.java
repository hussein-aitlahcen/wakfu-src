package com.ankamagames.baseImpl.graphics.script;

import org.apache.log4j.*;
import org.keplerproject.luajava.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.baseImpl.graphics.script.function.sound.*;
import com.ankamagames.framework.sound.group.*;
import java.io.*;
import com.ankamagames.framework.sound.stream.*;
import com.ankamagames.framework.sound.openAL.*;

public class SoundFunctionsLibrary extends JavaFunctionsLibrary
{
    protected static Logger m_logger;
    private static final SoundFunctionsLibrary m_instance;
    private SoundFunctionsLibraryDelegate m_delegate;
    private BarkProvider m_barkProvider;
    private GroundSoundProvider m_groundSoundProvider;
    
    @Override
    public final String getName() {
        return "Sound";
    }
    
    @Override
    public String getDescription() {
        return "NO Description<br/>Please Dev... implement me!";
    }
    
    private SoundFunctionsLibrary() {
        super();
        this.m_groundSoundProvider = new DefaultGroundSoundProvider();
    }
    
    public boolean canPlaySound() {
        return this.m_delegate != null;
    }
    
    public void setDelegate(final SoundFunctionsLibraryDelegate delegate) {
        this.m_delegate = delegate;
    }
    
    public SoundFunctionsLibraryDelegate getDelegate() {
        return this.m_delegate;
    }
    
    public BarkProvider getBarkProvider() {
        return this.m_barkProvider;
    }
    
    public void setBarkProvider(final BarkProvider barkProvider) {
        this.m_barkProvider = barkProvider;
    }
    
    public GroundSoundProvider getGroundSoundProvider() {
        return this.m_groundSoundProvider;
    }
    
    public void setGroundSoundProvider(final GroundSoundProvider groundSoundProvider) {
        this.m_groundSoundProvider = groundSoundProvider;
    }
    
    @Override
    public JavaFunctionEx[] createFunctions(final LuaState luaState) {
        return new JavaFunctionEx[] { new PlaySound(luaState), new SetMusicMix(luaState), new ResetMusicMix(luaState), new LoopSound(luaState), new Play3DSound(luaState), new PlayMusic(luaState), new StopMusic(luaState), new PlayRandomSound(luaState), new PlayApsSound(luaState), new PlayRandomApsSound(luaState), new PlayLocalSound(luaState), new StopSound(luaState), new PlayForcedMusic(luaState), new StopForcedMusic(luaState) };
    }
    
    @Override
    public JavaFunctionEx[] createGlobalFunctions(final LuaState luaState) {
        return null;
    }
    
    public static SoundFunctionsLibrary getInstance() {
        return SoundFunctionsLibrary.m_instance;
    }
    
    public AudioSourceDefinition playSound(final long soundId, final float gain, final int playCount, final long endDate, final long fadeOutDate, final int fightId) {
        if (this.m_delegate != null) {
            return this.m_delegate.playSound(soundId, gain, playCount, endDate, fadeOutDate, fightId);
        }
        SoundFunctionsLibrary.m_logger.debug((Object)"On essaie de jouer un son alors que le son n'est pas initialis?");
        return null;
    }
    
    public AudioSourceDefinition playSound(final long soundId, final float gain, final int playCount, final long endDate, final long fadeOutDate, final int fightId, final ObservedSource observed, final int rollOffPresetsId) {
        return this.playSound(soundId, gain, playCount, endDate, fadeOutDate, fightId, observed, rollOffPresetsId, true);
    }
    
    public AudioSourceDefinition playSound(final long soundId, final float gain, final int playCount, final long endDate, final long fadeOutDate, final int fightId, final ObservedSource observed, final int rollOffPresetsId, final boolean autoRelease) {
        if (this.m_delegate != null) {
            return this.m_delegate.playSound(soundId, gain, playCount, endDate, fadeOutDate, fightId, observed, rollOffPresetsId, autoRelease);
        }
        SoundFunctionsLibrary.m_logger.debug((Object)"On essaie de jouer un son alors que le son n'est pas initialis?");
        return null;
    }
    
    public void playForcedMusic(final long soundId) {
        if (this.m_delegate != null) {
            this.m_delegate.playForcedMusic(soundId);
        }
        SoundFunctionsLibrary.m_logger.debug((Object)"On essaie de jouer un son alors que le son n'est pas initialis?");
    }
    
    public void stopForcedMusic() {
        if (this.m_delegate != null) {
            this.m_delegate.stopForcedMusic();
        }
        SoundFunctionsLibrary.m_logger.debug((Object)"On essaie de jouer un son alors que le son n'est pas initialis?");
    }
    
    public GroundSoundData getGroundSoundData(final byte groundType, final byte walkType) {
        if (this.m_delegate != null && this.m_groundSoundProvider != null) {
            return this.m_groundSoundProvider.getSoundData(groundType, walkType);
        }
        return null;
    }
    
    public BarkData getBarkData(final int barkType, final ObservedSource observed, final int breedId) {
        if (this.m_delegate != null && this.m_barkProvider != null) {
            return this.m_barkProvider.getSoundId(observed, barkType, breedId);
        }
        return null;
    }
    
    public AudioSourceDefinition playBark(final int barkType, final int fightId, final float gain, final ObservedSource observed, final int breedId) {
        if (this.m_delegate == null || this.m_barkProvider == null) {
            SoundFunctionsLibrary.m_logger.debug((Object)"On essaie de jouer un son alors que le son n'est pas initialis?");
            return null;
        }
        final BarkData data = this.m_barkProvider.getSoundId(observed, barkType, breedId);
        if (data == null) {
            SoundFunctionsLibrary.m_logger.debug((Object)"Impossible de trouver de BarkData ad?quat");
            return null;
        }
        return this.m_delegate.playSound(data.m_soundId, data.m_gain * gain, 1, -1L, -1L, fightId, observed, data.getRollOff(), true);
    }
    
    public void resetLinkerMix() {
        if (this.m_delegate != null) {
            this.m_delegate.resetLinkerMix();
        }
        else {
            SoundFunctionsLibrary.m_logger.debug((Object)"appel ? resetLinkerMix alors que le son n'est pas initialis?");
        }
    }
    
    public void setLinkerMix(final float targetGain, final float fadeOutTime) {
        if (this.m_delegate != null) {
            this.m_delegate.setLinkerMix(targetGain, fadeOutTime);
        }
        else {
            SoundFunctionsLibrary.m_logger.debug((Object)"appel ? resetLinkerMix alors que le son n'est pas initialis?");
        }
    }
    
    public void stopSound(final long soundId, final AudioSource source) {
        this.m_delegate.stopSound(soundId, source);
    }
    
    static {
        SoundFunctionsLibrary.m_logger = Logger.getLogger((Class)SoundFunctionsLibrary.class);
        m_instance = new SoundFunctionsLibrary();
    }
    
    public static class DefaultSoundFunctionsLibraryDelegate implements SoundFunctionsLibraryDelegate
    {
        private AudioSourceGroup m_group;
        
        public DefaultSoundFunctionsLibraryDelegate(final AudioSourceGroup group) {
            super();
            if (group == null) {
                throw new IllegalArgumentException("le groupe d?fini est null !");
            }
            this.m_group = group;
        }
        
        @Override
        public final AudioSourceDefinition playSound(final long soundId, final float gain, final int playCount, final long endDate, final long fadeOutDate, final int fightId) {
            assert this.m_group != null : "Le groupe est null ! Comment est-ce possible ?";
            if (!this.m_group.getManager().isOpenALInitialized()) {
                SoundFunctionsLibrary.m_logger.warn((Object)"On essaie de jouer un son alors que le son n'est pas initialis?");
                return null;
            }
            AudioStreamProvider asp;
            try {
                asp = this.m_group.getHelper().fromId(soundId);
            }
            catch (IOException e) {
                return null;
            }
            if (asp == null) {
                return null;
            }
            final AudioSource source = this.m_group.prepareSound(asp, -1L);
            if (source == null) {
                return null;
            }
            source.setGain(gain);
            if (playCount == 0) {
                source.setLoop(true);
            }
            else if (playCount > 1) {
                source.setRepeatCount(playCount);
            }
            if (endDate != -1L) {
                source.setEndDate(endDate);
            }
            if (fadeOutDate != -1L) {
                source.setFadeOutDate(fadeOutDate);
            }
            this.m_group.addSource(source);
            return source;
        }
        
        @Override
        public AudioSourceDefinition playSound(final long soundId, final float gain, final int playCount, final long endDate, final long fadeOutDate, final int fightId, final ObservedSource observed, final int rollOffPresetId, final boolean autoRelease) {
            return this.playSound(soundId, gain, playCount, endDate, fadeOutDate, fightId);
        }
        
        @Override
        public void stopSound(final long soundId, final AudioSource source) {
            this.m_group.getManager();
            SoundManager.stopAndReleaseSound(source);
        }
        
        @Override
        public void playForcedMusic(final long soundId) {
        }
        
        @Override
        public void stopForcedMusic() {
        }
        
        @Override
        public void resetLinkerMix() {
        }
        
        @Override
        public void setLinkerMix(final float targetGain, final float fadeOutTime) {
        }
        
        @Override
        public void processSoundRequests() {
        }
    }
    
    public static class DefaultBarkProvider implements BarkProvider
    {
        @Override
        public BarkData getSoundId(final ObservedSource elem, final int type) {
            return null;
        }
        
        @Override
        public BarkData getSoundId(final ObservedSource elem, final int type, final int breed) {
            return null;
        }
    }
    
    public static class BarkData
    {
        private long m_soundId;
        private float m_gain;
        private int m_rollOff;
        
        public BarkData() {
            super();
            this.m_gain = 1.0f;
            this.m_rollOff = -1;
        }
        
        public long getSoundId() {
            return this.m_soundId;
        }
        
        public void setSoundId(final long soundId) {
            this.m_soundId = soundId;
        }
        
        public float getGain() {
            return this.m_gain;
        }
        
        public void setGain(final float gain) {
            this.m_gain = gain;
        }
        
        public int getRollOff() {
            return this.m_rollOff;
        }
        
        public void setRollOff(final int rollOff) {
            this.m_rollOff = rollOff;
        }
    }
    
    public static class DefaultGroundSoundProvider implements GroundSoundProvider
    {
        @Override
        public GroundSoundData getSoundData(final byte groundType, final byte walkType) {
            return null;
        }
        
        @Override
        public float getFightFspGain() {
            return 1.0f;
        }
    }
    
    public static class GroundSoundData
    {
        public static final byte WALK_TYPE = 0;
        public static final byte RUN_TYPE = 1;
        private long m_soundId;
        private float m_gain;
        private int m_rollOff;
        
        public GroundSoundData() {
            super();
            this.m_gain = 1.0f;
            this.m_rollOff = -1;
        }
        
        public long getSoundId() {
            return this.m_soundId;
        }
        
        public void setSoundId(final long soundId) {
            this.m_soundId = soundId;
        }
        
        public float getGain() {
            return this.m_gain;
        }
        
        public void setGain(final float gain) {
            this.m_gain = gain;
        }
        
        public int getRollOff() {
            return this.m_rollOff;
        }
        
        public void setRollOff(final int rollOff) {
            this.m_rollOff = rollOff;
        }
    }
    
    public interface SoundFunctionsLibraryDelegate
    {
        AudioSourceDefinition playSound(long p0, float p1, int p2, long p3, long p4, int p5);
        
        AudioSourceDefinition playSound(long p0, float p1, int p2, long p3, long p4, int p5, ObservedSource p6, int p7, boolean p8);
        
        void stopSound(long p0, AudioSource p1);
        
        void playForcedMusic(long p0);
        
        void stopForcedMusic();
        
        void resetLinkerMix();
        
        void setLinkerMix(float p0, float p1);
        
        void processSoundRequests();
    }
    
    public interface GroundSoundProvider
    {
        GroundSoundData getSoundData(byte p0, byte p1);
        
        float getFightFspGain();
    }
    
    public interface BarkProvider
    {
        BarkData getSoundId(ObservedSource p0, int p1);
        
        BarkData getSoundId(ObservedSource p0, int p1, int p2);
    }
}
