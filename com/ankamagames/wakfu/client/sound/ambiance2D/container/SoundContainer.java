package com.ankamagames.wakfu.client.sound.ambiance2D.container;

import com.ankamagames.framework.sound.group.*;
import org.apache.log4j.*;
import com.ankamagames.framework.sound.openAL.*;
import java.util.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.exporter.*;
import com.ankamagames.wakfu.client.sound.*;
import gnu.trove.*;

public abstract class SoundContainer<ASG extends AudioSourceGroup> extends AbstractSoundContainer<SoundContainer>
{
    protected static final Logger m_logger;
    private static final boolean DEBUG = false;
    public static final int PLAY_TYPE_SEQUENCE = 0;
    public static final int PLAY_TYPE_SHUFFLE = 1;
    public static final int TRANSITION_TYPE_CROSSFADE = 0;
    public static final int TRANSITION_TYPE_SILENCE = 1;
    public static final int LOOP_MODE_CONTINUOUS = 0;
    public static final int LOOP_MODE_STEP = 1;
    public static final int LOOP_MODE_READ_ONCE = 2;
    private final TLongArrayList m_sources;
    private boolean m_sortedSourcesUpToDate;
    protected final TLongArrayList m_sortedSources;
    private int m_sourceIndex;
    private int m_playType;
    private int m_transitionType;
    private int m_transitionInMinDuration;
    private int m_transitionInMaxDuration;
    private int m_transitionOutMinDuration;
    private int m_transitionOutMaxDuration;
    private boolean m_transitionUseSameInOutValues;
    private int m_maxShuffleListSize;
    private int m_shufflePlayCountLeft;
    private int m_loopDuration;
    private int m_loopMode;
    protected ASG m_group;
    private long m_playTime;
    private long m_currentTime;
    private long m_loopEndTime;
    private long m_currentSourceStartTime;
    private long m_currentSourceEndTime;
    private long m_currentSourceNextActionTime;
    private int m_currentSourceTransitionDuration;
    private long m_newSourceStartTime;
    private long m_newSourceEndTime;
    private long m_newSourceNextActionTime;
    private int m_newSourceTransitionDuration;
    private AudioSource m_currentSource;
    private AudioSource m_newSource;
    private int m_initialDelay;
    private long m_delayedPlayDate;
    private boolean m_playing;
    private SoundContainerListener m_listener;
    
    public SoundContainer() {
        super();
        this.m_sources = new TLongArrayList();
        this.m_sortedSourcesUpToDate = false;
        this.m_sortedSources = new TLongArrayList();
        this.m_sourceIndex = 0;
        this.m_playType = 0;
        this.m_transitionType = 0;
        this.m_transitionInMinDuration = 0;
        this.m_transitionInMaxDuration = 0;
        this.m_transitionOutMinDuration = 0;
        this.m_transitionOutMaxDuration = 0;
        this.m_transitionUseSameInOutValues = true;
        this.m_maxShuffleListSize = 5;
        this.m_loopMode = 0;
        this.m_initialDelay = 0;
        this.m_playing = false;
    }
    
    public void setGroup(final ASG group) {
        this.m_group = group;
    }
    
    public ASG getGroup() {
        return this.m_group;
    }
    
    public int getTransitionType() {
        return this.m_transitionType;
    }
    
    public void setTransitionType(final int transitionType) {
        this.m_transitionType = transitionType;
    }
    
    public int getTransitionInMinDuration() {
        return this.m_transitionInMinDuration;
    }
    
    public void setTransitionInMinDuration(final int transitionInMinDuration) {
        this.m_transitionInMinDuration = transitionInMinDuration;
        this.m_transitionInMaxDuration = Math.max(this.m_transitionInMinDuration, this.m_transitionInMaxDuration);
    }
    
    public int getTransitionInMaxDuration() {
        return this.m_transitionInMaxDuration;
    }
    
    public void setTransitionInMaxDuration(final int transitionInMaxDuration) {
        this.m_transitionInMaxDuration = transitionInMaxDuration;
        this.m_transitionInMinDuration = Math.min(this.m_transitionInMinDuration, this.m_transitionInMaxDuration);
    }
    
    public int getTransitionOutMinDuration() {
        return this.m_transitionOutMinDuration;
    }
    
    public void setTransitionOutMinDuration(final int transitionOutMinDuration) {
        this.m_transitionOutMinDuration = transitionOutMinDuration;
        this.m_transitionOutMaxDuration = Math.max(this.m_transitionOutMinDuration, this.m_transitionOutMaxDuration);
    }
    
    public int getTransitionOutMaxDuration() {
        return this.m_transitionOutMaxDuration;
    }
    
    public void setTransitionOutMaxDuration(final int transitionOutMaxDuration) {
        this.m_transitionOutMaxDuration = transitionOutMaxDuration;
        this.m_transitionOutMinDuration = Math.min(this.m_transitionOutMinDuration, this.m_transitionOutMaxDuration);
    }
    
    public int getLoopDuration() {
        return this.m_loopDuration;
    }
    
    public void setLoopDuration(final int loopDuration) {
        this.m_loopDuration = loopDuration;
    }
    
    public int getLoopMode() {
        return this.m_loopMode;
    }
    
    public void setLoopMode(final int loopMode) {
        this.m_loopMode = loopMode;
    }
    
    public int getPlayType() {
        return this.m_playType;
    }
    
    public void setPlayType(final int playType) {
        this.m_playType = playType;
    }
    
    public int getInitialDelay() {
        return this.m_initialDelay;
    }
    
    public void setInitialDelay(final int initialDelay) {
        this.m_initialDelay = initialDelay;
    }
    
    public void addSource(final long sourceId) {
        this.m_sources.add(sourceId);
        this.m_sortedSourcesUpToDate = false;
    }
    
    public void addSources(final long[] sourceIds) {
        this.m_sources.add(sourceIds);
        this.m_sortedSourcesUpToDate = false;
    }
    
    public void setListener(final SoundContainerListener listener) {
        this.m_listener = listener;
    }
    
    @Override
    public SoundContainer getValidSoundSource() {
        if (this.isValid()) {
            return this;
        }
        return null;
    }
    
    @Override
    public void getValidSoundSources(final ArrayList<SoundContainer> list) {
        if (this.isValid()) {
            list.add(this);
        }
    }
    
    @Override
    public void getValidSoundSources(final ArrayList<SoundContainer> list, final SoundEvent event) {
    }
    
    @Override
    public void getValidSoundSources(final ArrayList<SoundContainer> list, final AudioMarkerType type) {
    }
    
    @Override
    public void play(final long time) {
        this.stopAllSources();
        this.m_playTime = time;
        final boolean currentSourceTransitionDuration = false;
        this.m_currentSourceTransitionDuration = (currentSourceTransitionDuration ? 1 : 0);
        final long currentSourceEndTime = (long)(currentSourceTransitionDuration ? 1 : 0);
        this.m_currentSourceNextActionTime = currentSourceEndTime;
        this.m_currentSourceStartTime = currentSourceEndTime;
        this.m_currentSourceEndTime = currentSourceEndTime;
        if (this.isInContinuousMode()) {
            this.m_sourceIndex = 0;
        }
        if (this.m_initialDelay != 0) {
            this.m_delayedPlayDate = time + this.m_initialDelay;
            this.m_loopEndTime = ((this.m_loopDuration >= 0) ? (this.m_delayedPlayDate + this.m_loopDuration) : 0L);
            this.m_playing = true;
            return;
        }
        this.m_loopEndTime = ((this.m_loopDuration >= 0) ? (time + this.m_loopDuration) : 0L);
        this.m_playing = true;
    }
    
    @Override
    public void stop(final long time) {
        this.stop();
    }
    
    public void stop() {
        this.stopAllSources();
        this.m_playing = false;
    }
    
    public boolean update(final long time) {
        this.m_currentTime = time;
        if (!this.m_playing) {
            return true;
        }
        if (this.m_delayedPlayDate != 0L) {
            if (time < this.m_delayedPlayDate) {
                return true;
            }
            this.m_delayedPlayDate = 0L;
        }
        if (this.m_loopDuration != -1 && this.m_loopEndTime <= this.m_currentTime) {
            this.stopAllSources();
            return this.m_playing = false;
        }
        if (!this.isInContinuousMode()) {
            if (this.m_currentSource == null) {
                final long id = this.getNextSourceId();
                if (id == -1L) {
                    return false;
                }
                boolean loop = false;
                switch (this.m_loopMode) {
                    case 1: {
                        loop = true;
                        break;
                    }
                    case 2: {
                        loop = false;
                        break;
                    }
                }
                this.m_currentSource = this.createAudioSource(id, loop);
                if (this.m_currentSource == null) {
                    return false;
                }
                if (this.m_loopMode == 2) {
                    this.m_loopDuration = this.m_currentSource.getDurationInMs();
                    this.m_loopEndTime = ((this.m_initialDelay != 0) ? (this.m_delayedPlayDate + this.m_loopDuration) : (this.m_playTime + this.m_loopDuration));
                }
                if (this.m_listener != null) {
                    this.m_listener.onSourceCreation(this.m_currentSource);
                }
                this.m_currentSourceTransitionDuration = this.getTransitionOutDuration();
                final float realGain = this.getRealGain();
                if (this.isAudioMarker()) {
                    this.m_currentSource.setGain(realGain);
                    this.m_currentSource.setMaxGain(realGain);
                }
                else {
                    this.m_currentSource.setGain(0.0f);
                    this.m_currentSource.setMaxGain(realGain);
                    this.m_currentSource.fade(realGain, MathHelper.clamp(1000, 0, this.m_currentSource.getDurationInMs()));
                }
            }
            return true;
        }
        if (this.isTransitionCrossFade()) {
            if (time >= this.m_currentSourceNextActionTime && this.m_currentSourceNextActionTime != -1L) {
                if (this.m_newSource != null) {
                    this.stopSource(this.m_newSource, this.getTransitionOutDuration());
                }
                if (this.m_currentSource != null) {
                    this.stopSource(this.m_currentSource, true, this.m_currentSourceTransitionDuration);
                }
                final long nextId = this.getNextSourceId();
                if (nextId == -1L) {
                    return false;
                }
                this.m_newSource = this.createAudioSource(nextId, false);
                if (this.m_newSource != null) {
                    if (this.m_listener != null) {
                        this.m_listener.onSourceCreation(this.m_newSource);
                    }
                    this.m_newSourceStartTime = time;
                    this.m_newSourceEndTime = time + this.m_newSource.getDurationInMs();
                    this.m_newSourceTransitionDuration = this.getTransitionOutDuration();
                    this.m_newSourceNextActionTime = Math.max(this.m_newSourceStartTime, this.m_newSourceEndTime - this.m_newSourceTransitionDuration);
                    this.m_newSource.setGain(0.0f);
                    final float gain = this.getRealGain();
                    this.m_newSource.setMaxGain(gain);
                    this.m_newSource.fade(gain, this.getTransitionInDuration());
                }
                this.promoteNewSourceToCurrent();
            }
        }
        else {
            if (this.m_currentSourceNextActionTime == 0L) {
                this.m_currentSourceNextActionTime = time + this.getTransitionInDuration();
            }
            if (time >= this.m_currentSourceEndTime && this.m_currentSourceEndTime != 0L) {
                this.stopSource(this.m_currentSource, false, 0.0f);
                this.m_currentSource = null;
            }
            if (time >= this.m_currentSourceNextActionTime) {
                final long nextId = this.getNextSourceId();
                if (nextId == -1L) {
                    return false;
                }
                this.m_currentSource = this.createAudioSource(nextId, false);
                if (this.m_currentSource != null) {
                    if (this.m_listener != null) {
                        this.m_listener.onSourceCreation(this.m_currentSource);
                    }
                    this.m_currentSourceStartTime = time;
                    this.m_currentSourceEndTime = time + this.m_currentSource.getDurationInMs();
                    this.m_currentSourceTransitionDuration = this.getTransitionInDuration();
                    this.m_currentSourceNextActionTime = this.m_currentSourceEndTime + this.m_currentSourceTransitionDuration;
                    this.m_currentSource.setGain(this.getRealGain());
                }
            }
        }
        return true;
    }
    
    private void fireSourceCreation(final AudioSource source) {
        if (this.m_listener != null) {
            this.m_listener.onSourceCreation(source);
        }
    }
    
    protected abstract AudioSource createAudioSource(final long p0, final boolean p1);
    
    private int getTransitionInDuration() {
        return MathHelper.random(this.m_transitionInMinDuration, this.m_transitionInMaxDuration);
    }
    
    private int getTransitionOutDuration() {
        return MathHelper.random(this.m_transitionOutMinDuration, this.m_transitionOutMaxDuration);
    }
    
    private long getNextSourceId() {
        if (this.m_sources.size() == 0) {
            return -1L;
        }
        if (!this.m_sortedSourcesUpToDate) {
            this.sortSources();
            this.m_sourceIndex = 0;
        }
        if (this.m_sourceIndex >= this.m_sortedSources.size()) {
            if (this.m_playType == 1 && this.m_sortedSources.size() > 1 && this.m_shufflePlayCountLeft-- <= 0) {
                final long previousSourceId = this.m_sortedSources.getQuick(this.m_sortedSources.size() - 1);
                do {
                    this.sortSources();
                } while (previousSourceId == this.m_sortedSources.getQuick(0));
            }
            this.m_sourceIndex = 0;
        }
        final long sourceId = this.m_sortedSources.getQuick(this.m_sourceIndex);
        ++this.m_sourceIndex;
        return sourceId;
    }
    
    private boolean isTransitionCrossFade() {
        return this.m_transitionType == 0;
    }
    
    private boolean isInStepMode() {
        return this.m_loopMode == 1;
    }
    
    private boolean isInContinuousMode() {
        return this.m_loopMode == 0;
    }
    
    private float getRealGain() {
        final float gain = this.getGain();
        float maxGain = this.getMaxGain();
        if (maxGain == -1.0f) {
            return gain;
        }
        maxGain = Math.max(gain, maxGain);
        return MathHelper.random(gain, maxGain);
    }
    
    private void sortSources() {
        if (!this.m_sortedSourcesUpToDate || this.m_playType == 1) {
            this.m_sortedSources.clear();
            this.m_sortedSources.add(this.m_sources.toNativeArray());
            this.m_sortedSourcesUpToDate = true;
        }
        if (this.m_playType == 1) {
            this.m_sortedSources.shuffle(MathHelper.RANDOM);
            for (int i = this.m_sortedSources.size() - 1, size = this.m_maxShuffleListSize; i >= size; --i) {
                this.m_sortedSources.remove(i);
            }
            this.m_shufflePlayCountLeft = 20;
        }
    }
    
    private void stopAllSources() {
        if (this.m_currentSource != null) {
            this.stopSource(this.m_currentSource, true, 1000.0f);
            this.m_currentSource = null;
        }
        if (this.m_newSource != null) {
            this.stopSource(this.m_newSource, true, 1000.0f);
            this.m_newSource = null;
        }
    }
    
    private void stopSource(final AudioSource source, final float transition) {
        this.stopSource(source, this.isTransitionCrossFade(), transition);
    }
    
    private void stopSource(final AudioSource source, final boolean crossFade, final float duration) {
        if (source == null) {
            return;
        }
        if (crossFade) {
            source.fade(0.0f, duration);
            source.setStopOnNullGain(true);
        }
        else {
            source.stopAndRelease();
        }
    }
    
    private void promoteNewSourceToCurrent() {
        this.m_currentSource = this.m_newSource;
        this.m_currentSourceStartTime = this.m_newSourceStartTime;
        this.m_currentSourceNextActionTime = this.m_newSourceNextActionTime;
        this.m_currentSourceEndTime = this.m_newSourceEndTime;
        this.m_currentSourceTransitionDuration = this.m_newSourceTransitionDuration;
        this.m_newSource = null;
        this.m_newSourceStartTime = -1L;
        this.m_newSourceNextActionTime = -1L;
        this.m_newSourceEndTime = -1L;
        this.m_newSourceTransitionDuration = -1;
    }
    
    @Override
    protected void fillRawSoundContainer(final RawSoundContainer rsc) {
        super.fillRawSoundContainer(rsc);
        rsc.m_parentContainer = false;
        for (int i = 0, size = this.m_sources.size(); i < size; ++i) {
            rsc.m_soundSources.add(this.m_sources.get(i));
        }
        rsc.m_loopDuration = this.m_loopDuration;
        rsc.m_loopMode = this.m_loopMode;
        rsc.m_playType = this.m_playType;
        rsc.m_transition = this.m_transitionType;
        rsc.m_transitionInMinDuration = this.m_transitionInMinDuration;
        rsc.m_transitionInMaxDuration = this.m_transitionInMaxDuration;
        rsc.m_busId = GameSoundGroup.fromSourceGroup(this.m_group).getGroupId();
    }
    
    @Override
    public void forEachSource(final TObjectProcedure<SoundContainer> proc) {
        proc.execute(this);
    }
    
    public SoundContainer newInstanceWithParameters() {
        final SoundContainer soundContainer = this.newInstance();
        this.copy(soundContainer);
        return soundContainer;
    }
    
    protected abstract SoundContainer newInstance();
    
    @Override
    protected void copy(final AbstractSoundContainer c) {
        final SoundContainer container = (SoundContainer)c;
        super.copy(container);
        container.addSources(this.m_sources.toNativeArray());
        container.m_playType = this.m_playType;
        container.m_transitionType = this.m_transitionType;
        container.m_transitionInMinDuration = this.m_transitionInMinDuration;
        container.m_transitionInMaxDuration = this.m_transitionInMaxDuration;
        container.m_transitionOutMinDuration = this.m_transitionOutMinDuration;
        container.m_transitionOutMaxDuration = this.m_transitionOutMaxDuration;
        container.m_transitionUseSameInOutValues = this.m_transitionUseSameInOutValues;
        container.m_maxShuffleListSize = this.m_maxShuffleListSize;
        container.m_loopDuration = this.m_loopDuration;
        container.m_loopMode = this.m_loopMode;
        container.m_group = this.m_group;
        container.m_initialDelay = this.m_initialDelay;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SoundContainer.class);
    }
}
