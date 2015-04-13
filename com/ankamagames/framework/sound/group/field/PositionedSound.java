package com.ankamagames.framework.sound.group.field;

import org.apache.log4j.*;
import com.ankamagames.framework.sound.openAL.*;
import com.ankamagames.framework.sound.group.*;
import com.ankamagames.framework.kernel.core.maths.*;

public abstract class PositionedSound
{
    protected static final Logger m_logger;
    protected final AudioSource m_source;
    protected final ObservedSource m_position;
    private boolean m_releasable;
    protected final float m_maxDistance;
    private int m_minTimeBeforeLoop;
    private int m_maxTimeBeforeLoop;
    private boolean m_isReady;
    private long m_nextPlayDate;
    private boolean m_alreadyOutOfListenerRange;
    private boolean m_alreadyReleased;
    private boolean m_autoRelease;
    private float m_fadeDuration;
    private long m_lastPCFOffset;
    private float m_previousGain;
    private long m_timeStamp;
    private final Vector3 m_vector;
    
    public static PositionedSound getSound(final AudioSource source, final ObservedSource position, final float maxDistance, final boolean stereo, final boolean autoRelease, final float fadeDuration) {
        if (stereo) {
            return new PositionedStereoSound(source, position, maxDistance, autoRelease, fadeDuration);
        }
        return new PositionedMonoSound(source, position, maxDistance, autoRelease, fadeDuration);
    }
    
    public PositionedSound(final AudioSource source, final ObservedSource position, final float maxDistance, final boolean autoRelease, final float fadeDuration) {
        super();
        this.m_releasable = false;
        this.m_minTimeBeforeLoop = 0;
        this.m_maxTimeBeforeLoop = 0;
        this.m_isReady = false;
        this.m_nextPlayDate = -1L;
        this.m_alreadyOutOfListenerRange = false;
        this.m_alreadyReleased = false;
        this.m_autoRelease = false;
        this.m_fadeDuration = 0.0f;
        this.m_lastPCFOffset = 0L;
        this.m_previousGain = -1.0f;
        this.m_vector = new Vector3();
        this.m_source = source;
        this.m_position = position;
        this.m_maxDistance = maxDistance;
        this.m_alreadyOutOfListenerRange = false;
        this.m_alreadyReleased = true;
        this.m_autoRelease = autoRelease;
        this.m_fadeDuration = fadeDuration;
        this.m_timeStamp = -1L;
    }
    
    public void setMinMaxTimeBeforeLoop(final int minTimeLoop, final int maxTimeLoop) {
        this.m_minTimeBeforeLoop = minTimeLoop * 1000;
        this.m_maxTimeBeforeLoop = maxTimeLoop * 1000;
        final long currentTime = System.currentTimeMillis();
        this.m_nextPlayDate = currentTime + MathHelper.random(this.m_maxTimeBeforeLoop - this.m_minTimeBeforeLoop) + this.m_minTimeBeforeLoop;
    }
    
    public AudioSource getSource() {
        return this.m_source;
    }
    
    public void update(final long currentTime, final Vector3 listenerPosition, final float listenerDistance, final boolean isReady, final int maskKey) {
        if (isReady) {
            this.m_isReady = isReady;
        }
        else if (!this.m_isReady) {
            return;
        }
        if (this.m_timeStamp == -1L) {
            this.m_timeStamp = currentTime;
        }
        if (currentTime - this.m_timeStamp > this.m_source.getDurationInMs() && !this.m_source.isLoop()) {
            this.setReleasable(true);
            return;
        }
        if (this.m_previousGain == -1.0f) {
            this.m_previousGain = this.m_source.getGain();
        }
        this.m_vector.set(this.m_position.getObservedX(), this.m_position.getObservedY(), this.m_position.getObservedZ());
        if (!this.m_position.isPositionRelative()) {
            this.m_vector.subCurrent(listenerPosition);
        }
        final float distance = this.m_vector.length2D();
        if ((this.m_autoRelease && distance > this.m_maxDistance) || this.m_source.isFullyReleased()) {
            this.setReleasable(true);
            return;
        }
        this.m_source.setGroupInstanceId(this.m_position.getGroupInstanceId());
        if (distance <= this.m_maxDistance) {
            this.m_alreadyOutOfListenerRange = false;
        }
        else if (distance > this.m_maxDistance) {
            if (!this.m_alreadyOutOfListenerRange) {
                this.m_alreadyOutOfListenerRange = true;
                if (this.m_source.hasStartedProcessing()) {
                    this.m_source.setReleaseOnNullGain(true);
                    this.m_source.fade(0.0f, this.m_fadeDuration);
                }
            }
            else if (this.m_source.isStopOnNullGain()) {
                this.setReleasable(true);
            }
            if (this.m_alreadyReleased) {
                return;
            }
        }
        if (this.m_nextPlayDate == -1L) {
            Label_0423: {
                if (!this.m_alreadyReleased) {
                    if (this.m_source.isPlaying()) {
                        break Label_0423;
                    }
                }
                try {
                    if (this.m_alreadyReleased) {
                        if (!this.m_source.initializeOpenAL()) {
                            this.setReleasable(true);
                            return;
                        }
                        if (this.m_lastPCFOffset > 0L) {
                            this.m_source.pcmSeek(this.m_lastPCFOffset);
                        }
                        this.m_source.setGain(0.0f);
                        this.m_source.fade(this.m_previousGain, this.m_fadeDuration);
                        this.m_alreadyReleased = false;
                    }
                    this.m_source.play();
                }
                catch (Exception e) {
                    PositionedSound.m_logger.error((Object)"Exception", (Throwable)e);
                }
            }
            this.setRelativePosition(this.m_vector);
            switch (this.m_source.update(currentTime)) {
                case 0:
                case 2: {
                    this.m_lastPCFOffset = this.m_source.pcmTell();
                    break;
                }
                case 1: {
                    this.setReleasable(true);
                    break;
                }
                case 3: {
                    this.m_alreadyReleased = true;
                    break;
                }
            }
        }
        else if (this.m_nextPlayDate < currentTime) {
            Label_0620: {
                if (!this.m_alreadyReleased) {
                    if (this.m_source.isPlaying()) {
                        break Label_0620;
                    }
                }
                try {
                    if (this.m_alreadyReleased) {
                        if (!this.m_source.initializeOpenAL()) {
                            this.setReleasable(true);
                            return;
                        }
                        if (this.m_lastPCFOffset != -1L) {
                            this.m_source.pcmSeek(this.m_lastPCFOffset);
                        }
                        this.m_source.setGain(0.0f);
                        this.m_source.fade(this.m_previousGain, this.m_fadeDuration);
                        this.m_alreadyReleased = false;
                    }
                    this.m_source.play();
                }
                catch (Exception e) {
                    PositionedSound.m_logger.error((Object)"Exception", (Throwable)e);
                }
            }
            this.setRelativePosition(this.m_vector);
            switch (this.m_source.update(currentTime)) {
                case 0:
                case 2: {
                    this.m_lastPCFOffset = this.m_source.pcmTell();
                    break;
                }
                case 1: {
                    this.m_nextPlayDate = currentTime + MathHelper.random(this.m_maxTimeBeforeLoop - this.m_minTimeBeforeLoop) + this.m_minTimeBeforeLoop;
                    break;
                }
                case 3: {
                    this.m_alreadyReleased = true;
                    this.m_lastPCFOffset = this.m_source.pcmTell();
                    break;
                }
            }
        }
    }
    
    public boolean isReleasable() {
        return this.m_releasable;
    }
    
    public void release() {
        this.m_releasable = true;
    }
    
    public void modifyGain(final float v) {
        this.m_source.modifyGain(v);
    }
    
    public void modifyApparentGain(final float v) {
        this.m_source.modifyApparentGain(v);
    }
    
    public void updateGain() {
        this.m_source.updateGain();
    }
    
    public void updateApparentGain() {
        this.m_source.updateApparentGain();
    }
    
    public void setGainMod(final float gainMod) {
        this.m_source.setGainMod(gainMod);
    }
    
    public void setMaxGain(final float newMaxGain) {
        this.m_source.setMaxGain(newMaxGain);
    }
    
    public void onMuteChanged(final boolean newMute) {
        this.m_source.setMute(newMute);
    }
    
    public void setReleasable(final boolean releasable) {
        this.m_releasable = releasable;
    }
    
    public abstract void setRelativePosition(final Vector3 p0);
    
    @Override
    public String toString() {
        return this.m_source.toString();
    }
    
    static {
        m_logger = Logger.getLogger((Class)PositionedSound.class);
    }
}
