package com.ankamagames.framework.sound.group.defaultSound;

import com.ankamagames.framework.sound.group.*;
import com.ankamagames.framework.sound.*;
import com.ankamagames.framework.sound.stream.*;
import com.ankamagames.framework.sound.openAL.*;
import java.util.*;

public class DefaultSourceGroup extends AudioSourceGroup
{
    private static final byte MAX_CONCURRENT_SOUND = 8;
    private final LinkedList<AudioSource> m_sources;
    private final Object m_sourcesMutex;
    private final ArrayList<AudioSource> m_sourcesToAdd;
    private final ArrayList<AudioSource> m_sourcesToRemove;
    private VolumeLinker m_linker;
    private byte m_maxConcurrentSound;
    
    public DefaultSourceGroup(final String name) {
        super(name);
        this.m_sources = new LinkedList<AudioSource>();
        this.m_sourcesMutex = new Object();
        this.m_sourcesToAdd = new ArrayList<AudioSource>(5);
        this.m_sourcesToRemove = new ArrayList<AudioSource>(5);
        this.m_linker = null;
        this.m_maxConcurrentSound = 8;
    }
    
    public DefaultSourceGroup(final String name, final byte parentId) {
        super(name, parentId);
        this.m_sources = new LinkedList<AudioSource>();
        this.m_sourcesMutex = new Object();
        this.m_sourcesToAdd = new ArrayList<AudioSource>(5);
        this.m_sourcesToRemove = new ArrayList<AudioSource>(5);
        this.m_linker = null;
        this.m_maxConcurrentSound = 8;
    }
    
    public DefaultSourceGroup(final String name, final byte priority, final byte parentId) {
        super(name, priority, parentId);
        this.m_sources = new LinkedList<AudioSource>();
        this.m_sourcesMutex = new Object();
        this.m_sourcesToAdd = new ArrayList<AudioSource>(5);
        this.m_sourcesToRemove = new ArrayList<AudioSource>(5);
        this.m_linker = null;
        this.m_maxConcurrentSound = 8;
    }
    
    @Override
    public Collection<AudioSource> getSources() {
        return this.m_sources;
    }
    
    @Override
    public AudioSource addSource(final AudioStreamProvider asp, final boolean bStreaming, final boolean bStereo, final boolean bLoop, final long soundUID) {
        final AudioSource source = this.prepareSound(asp, -1L);
        if (source == null) {
            DefaultSourceGroup.m_logger.debug((Object)("Impossible d'initialiser une source audio : " + asp.getDescription()));
            return null;
        }
        if (this.addSource(source, true, true, bLoop)) {
            return source;
        }
        return null;
    }
    
    @Override
    public void addSource(final AudioSource source) {
        this.addSource(source, true, true, source.isLoop());
    }
    
    private synchronized boolean addSource(final AudioSource source, final boolean bStreaming, final boolean bStereo, final boolean bLoop) {
        if (source == null) {
            DefaultSourceGroup.m_logger.error((Object)"On ne peut pas jouer une source nulle");
            return false;
        }
        source.setGainMod(this.getGain());
        source.setMute(this.isMute());
        source.setLoop(bLoop);
        if (this.m_currentEffectSlot != 0) {
            source.applyReverbSetting(this.m_currentEffectSlot);
        }
        if (this.m_enableLowPassFilter) {
            source.setUseLowPassFilter(true);
            source.setGroupInstanceId(0);
        }
        synchronized (this.m_sourcesMutex) {
            if (this.m_sources.size() > this.m_maxConcurrentSound) {
                final SoundManager manager = this.m_manager;
                SoundManager.stopAndReleaseSound(source);
                return false;
            }
            try {
                source.play();
            }
            catch (Exception e) {
                DefaultSourceGroup.m_logger.error((Object)"Erreur durant la lecture de la source", (Throwable)e);
                final SoundManager manager2 = this.m_manager;
                SoundManager.stopAndReleaseSound(source);
                return false;
            }
            this.m_sourcesToAdd.add(source);
        }
        return true;
    }
    
    @Override
    public void addWaitingSources() {
        synchronized (this.m_sourcesMutex) {
            for (int i = 0, size = this.m_sourcesToAdd.size(); i < size; ++i) {
                final AudioSource source = this.m_sourcesToAdd.get(i);
                this.m_sources.add(source);
            }
            this.m_sourcesToAdd.clear();
        }
    }
    
    @Override
    public void onGainModChanged(final float newGainMod) {
        synchronized (this.m_sourcesMutex) {
            final float gain = this.getGain();
            for (final AudioSource source : this.m_sources) {
                source.setGainMod(gain);
            }
        }
    }
    
    @Override
    public void onGainChanged(final float previousGain, final float newGain) {
    }
    
    @Override
    public void onMaxGainChanged(final float previousMaxGain, final float newMaxGain) {
    }
    
    @Override
    public void onMuteChanged(final boolean previousMute, final boolean newMute) {
        synchronized (this.m_sourcesMutex) {
            for (final AudioSource source : this.m_sources) {
                source.setMute(newMute);
            }
        }
    }
    
    @Override
    public synchronized void update(final long currentTime) throws Exception {
        super.update(currentTime);
        synchronized (this.m_sourcesMutex) {
            int maskKey = 0;
            if (this.m_listener != null) {
                maskKey = this.m_listener.getGroupMaskKey();
            }
            for (int numSources = this.m_sources.size(), i = 0; i < numSources; ++i) {
                final AudioSource source = this.m_sources.get(i);
                switch (source.update(currentTime)) {
                    case 1:
                    case 3: {
                        this.m_sourcesToRemove.add(source);
                        final SoundManager manager = this.m_manager;
                        SoundManager.stopAndReleaseSound(source);
                        break;
                    }
                }
            }
            final int numSourcesToRemove = this.m_sourcesToRemove.size();
            for (int j = 0; j < numSourcesToRemove; ++j) {
                this.m_sources.remove(this.m_sourcesToRemove.get(j));
            }
            if (numSourcesToRemove > 0 && this.m_sources.size() == 0 && this.m_linker != null) {
                this.m_linker.onEnd();
            }
            this.m_sourcesToRemove.clear();
        }
    }
    
    @Override
    public void stop() throws Exception {
        synchronized (this.m_sourcesMutex) {
            for (int numSources = this.m_sources.size(), i = 0; i < numSources; ++i) {
                final SoundManager manager = this.m_manager;
                SoundManager.stopAndReleaseSound(this.m_sources.get(i));
            }
            this.m_sources.clear();
        }
    }
    
    @Override
    public void stopSource(final AudioSource source) {
        synchronized (this.m_sourcesMutex) {
            final boolean removed = this.m_sources.remove(source);
            if (removed) {
                final SoundManager manager = this.m_manager;
                SoundManager.stopAndReleaseSound(source);
            }
        }
    }
    
    @Override
    public synchronized boolean applyReverb(final int effectSlot) {
        if (!super.applyReverb(effectSlot)) {
            return false;
        }
        for (int i = 0, size = this.m_sources.size(); i < size; ++i) {
            this.m_sources.get(i).applyReverbSetting(effectSlot);
        }
        for (int i = 0, size = this.m_sourcesToAdd.size(); i < size; ++i) {
            this.m_sourcesToAdd.get(i).applyReverbSetting(effectSlot);
        }
        return true;
    }
    
    public void setLinker(final VolumeLinker linker) {
        this.m_linker = linker;
    }
    
    public void setLinkerTargetGain(final float targetGain) {
        if (this.m_linker != null) {
            this.m_linker.setTargetGain(targetGain);
        }
    }
    
    public void setLinkerFadeOutTime(final float fadeTime) {
        if (this.m_linker != null) {
            this.m_linker.setFadeOutTime(fadeTime);
        }
    }
    
    public void resetLinkerMix() {
        if (this.m_linker != null) {
            this.m_linker.resetTargetGain();
            this.m_linker.resetFadeOutTime();
        }
    }
}
