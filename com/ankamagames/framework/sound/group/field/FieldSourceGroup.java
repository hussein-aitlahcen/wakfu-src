package com.ankamagames.framework.sound.group.field;

import org.apache.log4j.*;
import gnu.trove.*;
import com.ankamagames.framework.sound.stream.*;
import com.ankamagames.framework.sound.group.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.sound.openAL.*;
import java.util.*;

public class FieldSourceGroup extends AudioSourceGroup
{
    protected static final Logger m_logger;
    private final ArrayList<PositionedSound> m_sources;
    private final ArrayList<PositionedSound> m_sourcesToAdd;
    private static final int[][] FIELDS_LAYOUT;
    private final TIntArrayList m_soundsToDelete;
    private float m_currentListenerDistance;
    private boolean m_isListenerReady;
    
    public FieldSourceGroup(final String name) {
        super(name);
        this.m_sources = new ArrayList<PositionedSound>();
        this.m_sourcesToAdd = new ArrayList<PositionedSound>();
        this.m_soundsToDelete = new TIntArrayList();
        this.m_currentListenerDistance = 1.0f;
        this.m_isListenerReady = true;
    }
    
    public FieldSourceGroup(final String name, final byte parentId) {
        super(name, parentId);
        this.m_sources = new ArrayList<PositionedSound>();
        this.m_sourcesToAdd = new ArrayList<PositionedSound>();
        this.m_soundsToDelete = new TIntArrayList();
        this.m_currentListenerDistance = 1.0f;
        this.m_isListenerReady = true;
    }
    
    public FieldSourceGroup(final String name, final byte priority, final byte parentId) {
        super(name, priority, parentId);
        this.m_sources = new ArrayList<PositionedSound>();
        this.m_sourcesToAdd = new ArrayList<PositionedSound>();
        this.m_soundsToDelete = new TIntArrayList();
        this.m_currentListenerDistance = 1.0f;
        this.m_isListenerReady = true;
    }
    
    @Override
    public AudioSource addSource(final AudioStreamProvider asp, final boolean bStreaming, final boolean bStereo, final boolean bLoop, final long soundUID) {
        return null;
    }
    
    @Override
    public void addSource(final AudioSource source) {
    }
    
    public synchronized PositionedSound addSource(final AudioStreamProvider asp, final float maxGain, final ObservedSource position, final float refDistance, final float maxDistance, final float rollOffFactor, final int minSilentTimeBeforeLoop, final int maxSilentTimeBeforeLoop, final boolean loop, final boolean stereo, final boolean autoRelease, final float fadeDuration, final long soundUID) {
        final AudioSource source = this.prepareSound(asp, soundUID);
        if (source == null) {
            FieldSourceGroup.m_logger.debug((Object)("le son " + asp.getDescription() + " ne peut pas \u00eatre pr\u00e9par\u00e9"));
            return null;
        }
        source.setLoop(loop && maxSilentTimeBeforeLoop == 0);
        source.setReferenceDistance(refDistance);
        source.setMaxDistance(maxDistance);
        source.setRolloffFactor((rollOffFactor != 0.0f) ? rollOffFactor : 1.0f);
        source.setMaxGain(maxGain);
        source.setGainMod(this.getGain());
        source.setMute(this.isMute());
        if (this.m_currentEffectSlot != 0) {
            source.applyReverbSetting(this.m_currentEffectSlot);
        }
        if (this.m_enableLowPassFilter) {
            source.setUseLowPassFilter(true);
        }
        source.releaseOpenALResourcesOnly();
        final PositionedSound positionedSoundPos = PositionedSound.getSound(source, position, maxDistance + 5.0f, stereo, autoRelease, fadeDuration);
        if (loop && maxSilentTimeBeforeLoop > 0) {
            positionedSoundPos.setMinMaxTimeBeforeLoop(minSilentTimeBeforeLoop, maxSilentTimeBeforeLoop);
        }
        synchronized (this.m_sources) {
            this.m_sourcesToAdd.add(positionedSoundPos);
        }
        return positionedSoundPos;
    }
    
    @Override
    public void addWaitingSources() {
        synchronized (this.m_sources) {
            for (int i = 0, size = this.m_sourcesToAdd.size(); i < size; ++i) {
                final PositionedSound source = this.m_sourcesToAdd.get(i);
                this.m_sources.add(source);
            }
            this.m_sourcesToAdd.clear();
        }
    }
    
    public void setListenerReady(final boolean ready) {
        this.m_isListenerReady = ready;
    }
    
    @Override
    public void onGainModChanged(final float newGainMod) {
        synchronized (this.m_sources) {
            final float gain = this.getGain();
            for (final PositionedSound source : this.m_sources) {
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
        synchronized (this.m_sources) {
            for (final PositionedSound source : this.m_sources) {
                source.onMuteChanged(newMute);
            }
        }
    }
    
    @Override
    public synchronized void update(final long currentTime) throws Exception {
        super.update(currentTime);
        final Vector3 listenerPosition = (this.m_listener != null) ? this.m_listener.getListenerPosition() : Vector3.ZERO;
        final float listenerDistance = (this.m_listener != null) ? this.m_listener.getListenerDistance() : 1.0f;
        final int maskKey = (this.m_listener != null) ? this.m_listener.getGroupMaskKey() : 0;
        this.m_currentListenerDistance = listenerDistance;
        synchronized (this.m_sources) {
            for (int i = 0, sourcesCount = this.m_sources.size(); i < sourcesCount; ++i) {
                final PositionedSound positionedSound = this.m_sources.get(i);
                positionedSound.update(currentTime, listenerPosition, listenerDistance, this.m_isListenerReady, maskKey);
                if (positionedSound.isReleasable()) {
                    this.m_soundsToDelete.add(i);
                    final SoundManager manager = this.m_manager;
                    SoundManager.stopAndReleaseSound(positionedSound.getSource());
                }
            }
            final int soundsToDelete = this.m_soundsToDelete.size();
            if (soundsToDelete > 0) {
                for (int j = soundsToDelete - 1; j >= 0; --j) {
                    this.m_sources.remove(this.m_soundsToDelete.getQuick(j));
                }
                this.m_soundsToDelete.resetQuick();
            }
        }
    }
    
    @Override
    public void stop() throws Exception {
        synchronized (this.m_sources) {
            for (final PositionedSound source : this.m_sources) {
                final SoundManager manager = this.m_manager;
                SoundManager.stopAndReleaseSound(source.getSource());
            }
            this.m_sources.clear();
        }
    }
    
    @Override
    public void stopSource(final AudioSource source) {
        synchronized (this.m_sources) {
            for (int i = this.m_sources.size() - 1; i >= 0; --i) {
                final PositionedSound pSound = this.m_sources.get(i);
                if (pSound.getSource() == source) {
                    this.m_sources.remove(i);
                    final SoundManager manager = this.m_manager;
                    SoundManager.stopAndReleaseSound(source);
                }
            }
        }
    }
    
    @Override
    public synchronized boolean applyReverb(final int effectSlot) {
        if (!super.applyReverb(effectSlot)) {
            return false;
        }
        for (int i = 0, size = this.m_sources.size(); i < size; ++i) {
            this.m_sources.get(i).getSource().applyReverbSetting(effectSlot);
        }
        for (int i = 0, size = this.m_sourcesToAdd.size(); i < size; ++i) {
            this.m_sourcesToAdd.get(i).getSource().applyReverbSetting(effectSlot);
        }
        return true;
    }
    
    @Override
    public Collection<AudioSource> getSources() {
        throw new UnsupportedOperationException("Pas d'acc\u00e8s direct \u00e0 la liste des sources du FieldSourceGroup");
    }
    
    public int getSourcesSize() {
        return this.m_sources.size();
    }
    
    public String getSourcesDescription() {
        final StringBuffer stringBuffer = new StringBuffer();
        for (final PositionedSound positionedSound : this.m_sources) {
            stringBuffer.append(positionedSound.getSource().getDescription()).append("\n");
        }
        return stringBuffer.toString();
    }
    
    @Override
    public String toString() {
        return "[FieldSourceGroup-" + this.getName() + "] " + this.m_sources.size() + " + " + this.m_sourcesToAdd.size() + " sources en cours de lecture.";
    }
    
    static {
        m_logger = Logger.getLogger((Class)FieldSourceGroup.class);
        FIELDS_LAYOUT = new int[][] { { -1, -1 }, { 0, -1 }, { 1, -1 }, { -1, 0 }, { 0, 0 }, { 1, 0 }, { -1, 1 }, { 0, 1 }, { 1, 1 } };
    }
}
