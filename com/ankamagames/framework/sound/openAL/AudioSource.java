package com.ankamagames.framework.sound.openAL;

import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.framework.sound.*;
import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.framework.sound.stream.*;
import com.ankamagames.framework.sound.group.*;
import org.lwjgl.*;
import org.lwjgl.openal.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.java.util.*;

public class AudioSource implements AudioSourceDefinition, Poolable, GainModifiable
{
    public static final int PLAYING_STATE = 0;
    public static final int STOPPED_STATE = 1;
    public static final int PAUSED_STATE = 2;
    public static final int RELEASED_STATE = 3;
    protected static final Logger m_logger;
    protected static final boolean DEBUG_MODE = false;
    protected static final int BUFFER_SIZE = 65536;
    protected static final int NUM_BUFFER = 2;
    private long m_soundUID;
    protected int m_bufferCount;
    protected IntBuffer m_buffers;
    protected IntBuffer m_source;
    private final ByteBuffer m_pcmBuffer;
    private int m_format;
    private int m_voicesNeeded;
    private int m_voicesUsed;
    private boolean m_groupVoices;
    private int m_rate;
    private float[] m_position;
    private float m_rollOffFactor;
    private float m_refDistance;
    private float m_maxDistance;
    private boolean m_useLowPassFilter;
    private int m_groupInstanceId;
    private int m_reverbEffectSlot;
    private int m_lowPassFilter;
    protected volatile AudioStream m_stream;
    private volatile boolean m_soundManagerRelease;
    private SoundManager m_manager;
    private AudioSourceGroup m_group;
    protected volatile float m_minGain;
    protected volatile float m_maxGain;
    protected volatile float m_gain;
    protected volatile float m_gainMod;
    protected volatile float m_gainVariation;
    protected volatile boolean m_stopOnNullGain;
    protected volatile boolean m_pauseOnNullGain;
    protected volatile boolean m_releaseOnNullGain;
    protected volatile boolean m_stopped;
    protected volatile boolean m_startedProcessing;
    protected volatile boolean m_muted;
    protected volatile boolean m_loop;
    protected volatile long m_endDate;
    protected volatile long m_fadeOutDate;
    private volatile int m_playCount;
    private volatile boolean m_isActive;
    private volatile boolean m_isPaused;
    private volatile boolean m_isReleased;
    private volatile boolean m_justRefilled;
    
    public AudioSource() {
        super();
        this.m_bufferCount = 2;
        this.m_buffers = BufferUtils.createIntBuffer(2);
        this.m_source = BufferUtils.createIntBuffer(1);
        this.m_pcmBuffer = BufferUtils.createByteBuffer(65536);
        this.m_groupVoices = true;
        this.m_position = new float[3];
        this.m_rollOffFactor = 1.0f;
        this.m_refDistance = 1.0f;
        this.m_maxDistance = Float.MAX_VALUE;
        this.m_useLowPassFilter = false;
        this.m_groupInstanceId = -1;
        this.m_reverbEffectSlot = 0;
        this.m_lowPassFilter = 0;
        this.m_soundManagerRelease = false;
        this.m_stopOnNullGain = false;
        this.m_pauseOnNullGain = false;
        this.m_releaseOnNullGain = false;
        this.m_stopped = false;
        this.m_startedProcessing = false;
        this.m_muted = false;
        this.m_loop = false;
        this.m_endDate = 0L;
        this.m_fadeOutDate = 0L;
        this.m_isActive = true;
        this.m_isPaused = false;
        this.m_isReleased = false;
        this.m_justRefilled = false;
    }
    
    @Override
    public long getSoundUID() {
        return this.m_soundUID;
    }
    
    public void setSoundUID(final long soundUID) {
        this.m_soundUID = soundUID;
    }
    
    public byte getPriority() {
        if (this.m_group != null) {
            return this.m_group.getPriority();
        }
        return 0;
    }
    
    public void setManager(final SoundManager manager) {
        this.m_manager = manager;
    }
    
    public AudioSourceGroup getGroup() {
        return this.m_group;
    }
    
    public AudioStream getStream() {
        return this.m_stream;
    }
    
    public boolean isSoundManagerRelease() {
        return this.m_soundManagerRelease;
    }
    
    public void setSoundManagerRelease(final boolean soundManagerRelease) {
        this.m_soundManagerRelease = soundManagerRelease;
    }
    
    public boolean isLoop() {
        return this.m_loop;
    }
    
    public void setLoop(final boolean loop) {
        this.m_loop = loop;
    }
    
    @Override
    public void onCheckOut() {
        this.m_muted = false;
        this.m_stopOnNullGain = false;
        this.m_pauseOnNullGain = false;
        this.m_releaseOnNullGain = false;
        this.m_stopped = false;
        this.m_gainVariation = 0.0f;
        this.m_loop = false;
        this.m_minGain = 0.0f;
        this.m_maxGain = 1.0f;
        this.m_gainMod = 1.0f;
        this.m_gain = this.m_maxGain;
        this.m_endDate = 0L;
        this.m_fadeOutDate = 0L;
        this.m_playCount = 1;
        this.m_isActive = true;
        this.m_isPaused = false;
        final float[] position = this.m_position;
        final int n = 0;
        final float[] position2 = this.m_position;
        final int n2 = 1;
        final float[] position3 = this.m_position;
        final int n3 = 2;
        final float n4 = 0.0f;
        position3[n3] = n4;
        position[n] = (position2[n2] = n4);
        this.m_rollOffFactor = 1.0f;
        this.m_refDistance = 1.0f;
        this.m_maxDistance = Float.MAX_VALUE;
        this.m_isReleased = false;
        this.m_reverbEffectSlot = 0;
        this.m_lowPassFilter = 0;
        this.m_useLowPassFilter = false;
        this.m_groupInstanceId = 0;
    }
    
    @Override
    public void onCheckIn() {
        this.cleanUp();
        if (this.m_manager != null) {
            this.m_manager.unregisterToLowPassFilter(this.m_groupInstanceId, this.m_soundUID);
        }
        this.m_bufferCount = 2;
    }
    
    public synchronized boolean stream(final int buffer) {
        boolean ret = true;
        try {
            this.m_pcmBuffer.clear();
            int size = this.m_stream.read(this.m_pcmBuffer, 0);
            final boolean endOfStream = size <= 0;
            size = Math.abs(size);
            if (endOfStream) {
                if (this.m_loop || --this.m_playCount > 0) {
                    this.m_stream.reset();
                    size += Math.abs(this.m_stream.read(this.m_pcmBuffer, size));
                }
                else {
                    ret = false;
                }
            }
            this.m_pcmBuffer.position(0).limit(size);
            AL10.alBufferData(buffer, this.m_format, this.m_pcmBuffer, this.m_rate);
            AL10.alSourceQueueBuffers(this.m_source.get(0), buffer);
        }
        catch (Exception e) {
            AudioSource.m_logger.error((Object)"Exeption lev\u00e9e", (Throwable)e);
            return false;
        }
        return ret;
    }
    
    public synchronized boolean initializeOpenAL() {
        if (!this.initializeOpenALOnly()) {
            return false;
        }
        this.m_isReleased = false;
        if (this.m_reverbEffectSlot != 0) {
            this.applyReverbSetting(this.m_reverbEffectSlot);
        }
        if (this.m_lowPassFilter != 0) {
            this.applyLowPassSetting(this.m_lowPassFilter);
        }
        this.setMute(this.m_muted);
        this.setMinGain(this.m_minGain);
        this.setMaxGain(this.m_maxGain);
        this.setGain(this.m_gain);
        this.setRolloffFactor(this.m_rollOffFactor);
        this.setReferenceDistance(this.m_refDistance);
        this.setMaxDistance(this.m_maxDistance);
        if (this.m_stream != null) {
            this.m_stream.reinitialize();
        }
        return true;
    }
    
    private synchronized boolean initializeOpenALOnly() {
        while (true) {
            while (!this.m_group.checkOutVoices(this.m_voicesNeeded)) {
                if (this.m_manager.checkOutVoices(this.m_voicesNeeded)) {
                    this.m_voicesUsed = this.m_voicesNeeded;
                    this.m_groupVoices = false;
                    try {
                        this.m_buffers.clear();
                        AL10.alGenBuffers(this.m_buffers);
                        final SoundManager manager = this.m_manager;
                        SoundManager.check();
                    }
                    catch (Exception e) {
                        AudioSource.m_logger.warn((Object)"Impossible de cr\u00e9er un buffer suppl\u00e9mentaire.", (Throwable)e);
                        return false;
                    }
                    try {
                        this.m_source.clear();
                        AL10.alGenSources(this.m_source);
                        final SoundManager manager2 = this.m_manager;
                        SoundManager.check();
                    }
                    catch (Exception e) {
                        AudioSource.m_logger.warn((Object)"Impossible de cr\u00e9er une source audio suppl\u00e9mentaire.");
                        return false;
                    }
                    AL10.alSourcei(this.m_source.get(0), 4103, 0);
                    AL10.alSourcef(this.m_source.get(0), 4099, 1.0f);
                    AL10.alSourcei(this.m_source.get(0), 514, 1);
                    return true;
                }
                final AudioSource s = AudioSourceManager.getInstance().getSourceWithLesserPriority();
                if (s == null) {
                    AudioSource.m_logger.debug((Object)"Il n'y a aucune source \u00e0 lib\u00e9rer, mais on n'arrive pas \u00e0 r\u00e9server de voix");
                    this.m_voicesUsed = 0;
                    return false;
                }
                if (s.getPriority() <= this.m_group.getPriority()) {
                    AudioSource.m_logger.debug((Object)"Impossible d'assigner suffisament de voix \u00e0 cette Source.");
                    this.m_voicesUsed = 0;
                    return false;
                }
                s.stopAndRelease();
            }
            this.m_voicesUsed = this.m_voicesNeeded;
            this.m_groupVoices = true;
            continue;
        }
    }
    
    public synchronized boolean initialize(final SoundManager manager, final AudioSourceGroup group, final AudioStream stream) {
        this.m_manager = manager;
        this.m_group = group;
        this.m_stream = stream;
        if (this.m_stream.getNumChannels() == 1) {
            this.m_voicesNeeded = 1;
            this.m_format = 4353;
        }
        else {
            this.m_voicesNeeded = 2;
            this.m_format = 4355;
        }
        this.m_rate = this.m_stream.getSampleRate();
        if (!this.initializeOpenALOnly()) {
            return false;
        }
        this.setMaxGain(1.0f);
        this.setMinGain(0.0f);
        this.setGainMod(1.0f);
        this.setGain(this.getMaxGain());
        return true;
    }
    
    public synchronized void applyReverbSetting(final int effectSlot) {
        this.m_reverbEffectSlot = effectSlot;
        if (this.m_source.get(0) != 0) {
            AL11.alSource3i(this.m_source.get(0), 131078, effectSlot, 0, 0);
        }
    }
    
    public synchronized void applyLowPassSetting(final int filter) {
        this.m_lowPassFilter = filter;
        if (this.m_source.get(0) != 0) {
            AL10.alSourcei(this.m_source.get(0), 131077, this.m_useLowPassFilter ? this.m_lowPassFilter : 0);
        }
    }
    
    public synchronized void setUseLowPassFilter(final boolean useLowPassFilter) {
        if (this.m_useLowPassFilter == useLowPassFilter) {
            return;
        }
        this.m_useLowPassFilter = useLowPassFilter;
        if (this.m_useLowPassFilter) {
            this.m_manager.registerToLowPassFilter(this.m_groupInstanceId, this.m_soundUID);
        }
        else {
            this.m_manager.unregisterToLowPassFilter(this.m_groupInstanceId, this.m_soundUID);
        }
        this.applyLowPassSetting(this.m_lowPassFilter);
    }
    
    public int getGroupInstanceId() {
        return this.m_groupInstanceId;
    }
    
    public void setGroupInstanceId(final int groupInstanceId) {
        if (this.m_groupInstanceId == groupInstanceId) {
            return;
        }
        this.m_manager.unregisterToLowPassFilter(this.m_groupInstanceId, this.m_soundUID);
        this.m_groupInstanceId = groupInstanceId;
        this.m_manager.registerToLowPassFilter(this.m_groupInstanceId, this.m_soundUID);
    }
    
    public synchronized void releaseOpenALResourcesOnly() {
        this.releaseOpenALResources();
        if (this.m_stream != null) {
            this.m_stream.close();
        }
        this.m_isReleased = true;
    }
    
    private synchronized void releaseOpenALResources() {
        if (this.m_source.get(0) != 0) {
            AL10.alSourceStop(this.m_source.get(0));
            this.cleanUpQueuedBuffers();
        }
        if (this.m_source.get(0) != 0) {
            this.m_source.position(0);
            AL10.alDeleteSources(this.m_source);
            this.m_source.put(0, 0);
            this.m_source.clear();
        }
        for (int i = 0; i < 2; ++i) {
            final int buffer = this.m_buffers.get(i);
            if (buffer != 0) {
                this.m_buffers.position(0);
                AL10.alDeleteBuffers(buffer);
                this.m_buffers.put(i, 0);
            }
            this.m_buffers.clear();
        }
        if (this.m_voicesUsed != 0) {
            if (this.m_groupVoices) {
                this.m_group.checkInVoices(this.m_voicesUsed);
            }
            else {
                this.m_manager.checkInVoices(this.m_voicesUsed);
            }
            this.m_voicesUsed = 0;
        }
        this.m_stopped = false;
        this.m_startedProcessing = false;
        this.m_justRefilled = false;
    }
    
    private synchronized void cleanUp() {
        if (this.m_group == null) {
            return;
        }
        this.releaseOpenALResources();
        if (this.m_stream != null) {
            this.m_stream.close();
            this.m_stream.subRefCount();
            this.m_stream = null;
        }
        this.m_voicesUsed = 0;
        this.m_muted = false;
        this.m_gainVariation = 0.0f;
        this.m_gain = this.m_maxGain;
        this.m_group = null;
    }
    
    private synchronized void cleanUpQueuedBuffers() {
        if (this.m_group == null || this.m_isReleased) {
            return;
        }
        for (int buffersQueued = AL10.alGetSourcei(this.m_source.get(0), 4117), i = 0; i < buffersQueued; ++i) {
            AL10.alSourceUnqueueBuffers(this.m_source.get(0));
        }
    }
    
    public synchronized void play() throws Exception {
        this.m_stopped = false;
    }
    
    public synchronized int update(final long currentTime) {
        if (this.m_group == null) {
            return 1;
        }
        if (this.m_endDate > 0L && currentTime > this.m_endDate) {
            this.stop();
            return 1;
        }
        if (this.m_stopped) {
            return 1;
        }
        if (this.m_isPaused) {
            return 2;
        }
        boolean bufferMightHaveChanged = false;
        if (!this.m_startedProcessing) {
            for (int i = 0; i < this.m_bufferCount; ++i) {
                this.stream(this.m_buffers.get(i));
            }
            this.m_justRefilled = true;
            bufferMightHaveChanged = true;
            this.m_startedProcessing = true;
        }
        else {
            final int numProcessed = AL10.alGetSourcei(this.m_source.get(0), 4118);
            if (numProcessed > 0) {
                if (numProcessed > 4) {
                    AudioSource.m_logger.error((Object)("Processed buffers > 4 : " + numProcessed));
                    try {
                        final SoundManager manager = this.m_manager;
                        SoundManager.check();
                    }
                    catch (Exception e) {
                        AudioSource.m_logger.error((Object)"Exception ", (Throwable)e);
                    }
                }
                else {
                    for (int num = 0; num < numProcessed; ++num) {
                        final int buffer = AL10.alSourceUnqueueBuffers(this.m_source.get(0));
                        if (this.m_isActive) {
                            this.m_isActive = this.stream(buffer);
                            this.m_justRefilled = true;
                        }
                    }
                    bufferMightHaveChanged = true;
                }
            }
            else {
                this.m_justRefilled = false;
            }
        }
        if (bufferMightHaveChanged && !this.isPlaying()) {
            AL10.alSourcePlay(this.m_source.get(0));
        }
        if (this.m_fadeOutDate > 0L && currentTime > this.m_fadeOutDate) {
            this.m_gainVariation = this.m_gain / (this.m_endDate - this.m_fadeOutDate);
        }
        if (this.m_gainVariation != 0.0f) {
            this.setGain(this.m_gain + this.m_gainVariation);
        }
        if (this.m_gainVariation < 0.0f && this.m_gain == this.m_minGain) {
            this.m_gainVariation = 0.0f;
        }
        else if (this.m_gainVariation > 0.0f && this.m_gain == this.m_maxGain) {
            this.m_gainVariation = 0.0f;
        }
        if ((!this.m_stopOnNullGain && !this.m_pauseOnNullGain && !this.m_releaseOnNullGain) || this.m_gain != this.m_minGain) {
            return (!this.m_isActive && !this.isPlaying()) ? 1 : 0;
        }
        this.m_gainVariation = 0.0f;
        if (this.m_stopOnNullGain) {
            this.stop();
            return 1;
        }
        if (this.m_pauseOnNullGain) {
            this.pause();
            return 2;
        }
        this.releaseOpenALResourcesOnly();
        return 3;
    }
    
    private int getSourceState() {
        if (this.m_group != null && !this.m_isReleased) {
            final int sourceState = AL10.alGetSourcei(this.m_source.get(0), 4112);
            return sourceState;
        }
        return -1;
    }
    
    public synchronized boolean hasStartedProcessing() {
        return this.m_startedProcessing;
    }
    
    public synchronized boolean isActive() {
        return this.m_isActive;
    }
    
    public synchronized boolean isReleased() {
        return this.m_isReleased;
    }
    
    public synchronized boolean isFullyReleased() {
        return this.m_group == null;
    }
    
    public synchronized boolean isPlaying() {
        return this.getSourceState() == 4114;
    }
    
    public synchronized void pause() {
        if (this.m_isPaused) {
            return;
        }
        final int state = this.getSourceState();
        if ((state == 4114 || state == 4113 || state == 4115) && this.m_group != null && !this.m_isReleased) {
            AL10.alSourceStop(this.m_source.get(0));
        }
        this.m_isPaused = true;
    }
    
    public synchronized void unPause() {
        this.m_isPaused = false;
    }
    
    public synchronized void stop() {
        final int state = this.getSourceState();
        if (state == 4114 || state == 4113 || state == 4115) {
            if (!this.m_isReleased) {
                AL10.alSourceStop(this.m_source.get(0));
            }
            this.cleanUpQueuedBuffers();
        }
        this.m_startedProcessing = false;
        this.m_isActive = true;
        if (this.m_stream != null) {
            this.m_stream.reset();
        }
    }
    
    public synchronized void close() {
        if (this.m_stream != null) {
            this.m_stream.close();
        }
    }
    
    public synchronized void stopAndRelease() {
        this.cleanUp();
    }
    
    public void modifyGain(final float gainMod) {
        float gain = this.getGain();
        gain *= gainMod;
        this.setGain(gain);
    }
    
    public void modifyApparentGain(final float gainMod) {
        float gain = this.getGain();
        gain *= gainMod;
        this.setApparentGain(gain);
    }
    
    public void updateGain() {
        this.setGain(this.getGain());
    }
    
    public void updateApparentGain() {
        this.setApparentGain(this.getGain());
    }
    
    public synchronized void setMute(final boolean mute) {
        if (!this.m_isReleased) {
            AL10.alSourcef(this.m_source.get(0), 4106, mute ? 0.0f : (this.m_gain * this.m_gainMod));
        }
        this.m_muted = mute;
    }
    
    public synchronized void setGain(final float gain) {
        this.m_gain = Math.min(this.m_maxGain, Math.max(this.m_minGain, gain));
        if (!this.m_isReleased) {
            if (this.m_muted) {
                AL10.alSourcef(this.m_source.get(0), 4106, 0.0f);
            }
            else {
                AL10.alSourcef(this.m_source.get(0), 4106, this.m_gain * this.m_gainMod);
            }
        }
    }
    
    public synchronized void setApparentGain(float gain) {
        gain = Math.min(this.m_maxGain, Math.max(this.m_minGain, gain));
        if (!this.m_isReleased) {
            if (this.m_muted) {
                AL10.alSourcef(this.m_source.get(0), 4106, 0.0f);
            }
            else {
                AL10.alSourcef(this.m_source.get(0), 4106, gain * this.m_gainMod);
            }
        }
    }
    
    public int getDurationInMs() {
        if (this.m_stream != null) {
            return this.m_stream.getDurationInMs();
        }
        return 0;
    }
    
    @Override
    public float getGain() {
        return this.m_gain;
    }
    
    public void setMaxGain(final float gain) {
        this.m_maxGain = Math.min(1.0f, Math.max(0.0f, gain));
        if (this.m_maxGain < this.m_minGain) {
            final float t = this.m_maxGain;
            this.m_maxGain = this.m_minGain;
            this.m_minGain = t;
        }
        this.setGain(this.m_gain);
    }
    
    public void setMinGain(final float gain) {
        this.m_minGain = Math.min(1.0f, Math.max(0.0f, gain));
        if (this.m_maxGain < this.m_minGain) {
            final float t = this.m_maxGain;
            this.m_maxGain = this.m_minGain;
            this.m_minGain = t;
        }
    }
    
    public float getMinGain() {
        return this.m_minGain;
    }
    
    public float getMaxGain() {
        return this.m_maxGain;
    }
    
    public float getGainMod() {
        return this.m_gainMod;
    }
    
    public void setGainMod(final float gainMod) {
        this.m_gainMod = gainMod;
        this.setGain(this.getGain());
    }
    
    public synchronized void setReferenceDistance(final float distance) {
        this.m_refDistance = distance;
        if (!this.m_isReleased) {
            AL10.alSourcef(this.m_source.get(0), 4128, distance);
        }
    }
    
    public synchronized void setMaxDistance(final float distance) {
        this.m_maxDistance = distance;
        if (!this.m_isReleased) {
            AL10.alSourcef(this.m_source.get(0), 4131, distance);
        }
    }
    
    public synchronized void setRolloffFactor(final float factor) {
        this.m_rollOffFactor = factor;
        if (!this.m_isReleased) {
            AL10.alSourcef(this.m_source.get(0), 4129, factor);
        }
    }
    
    public synchronized void setPosition(final Vector3 position) {
        this.m_position[0] = position.getX();
        this.m_position[1] = position.getY();
        this.m_position[2] = position.getZ();
        if (!this.m_isReleased) {
            AL10.alSource3f(this.m_source.get(0), 4100, this.m_position[0], this.m_position[1], this.m_position[2]);
        }
    }
    
    public synchronized void setPosition(final float x, final float y, final float z) {
        this.m_position[0] = x;
        this.m_position[1] = y;
        this.m_position[2] = z;
        if (!this.m_isReleased) {
            AL10.alSource3f(this.m_source.get(0), 4100, this.m_position[0], this.m_position[1], this.m_position[2]);
            try {
                final SoundManager manager = this.m_manager;
                SoundManager.check();
            }
            catch (Exception e) {
                AudioSource.m_logger.error((Object)"Exception", (Throwable)e);
            }
        }
    }
    
    public long rawTell() {
        if (this.m_stream != null) {
            return this.m_stream.rawTell();
        }
        return -1L;
    }
    
    public synchronized int rawSeek(final long offset) {
        if (this.m_stream != null) {
            return this.m_stream.rawSeek(offset);
        }
        return -1;
    }
    
    public synchronized long pcmTell(final boolean minusBuffers) {
        if (this.m_stream == null || this.m_group == null || this.m_isReleased) {
            return -1L;
        }
        final long pcmTell = this.m_stream.pcmTell();
        if (minusBuffers) {
            final int buffersQueued = AL10.alGetSourcei(this.m_source.get(0), 4117);
            return this.m_stream.pcmTell() - 65536 / this.m_stream.getCurrentBytesPerSample() * buffersQueued;
        }
        return pcmTell;
    }
    
    public long pcmTell() {
        return this.pcmTell(true);
    }
    
    public synchronized int pcmSeek(final long offset) {
        if (this.m_stream != null) {
            return this.m_stream.pcmSeek(offset);
        }
        return -1;
    }
    
    public float timeTell() {
        if (this.m_stream == null) {
            return 0.0f;
        }
        return this.m_stream.timeTell();
    }
    
    public synchronized int timeSeek(final float seconds) {
        if (this.m_stream != null) {
            return this.m_stream.timeSeek(seconds);
        }
        return -1;
    }
    
    public boolean isJustRefilled() {
        return this.m_justRefilled;
    }
    
    public void fadeOutAndStop(final float factor) {
        this.m_stopOnNullGain = true;
        this.fadeOut(factor);
    }
    
    public void fadeOutAndPause(final float factor) {
        this.m_pauseOnNullGain = true;
        this.fadeOut(factor);
    }
    
    public void fadeIn(final float factor) {
        this.m_gainVariation = factor;
    }
    
    @Override
    public void fade(final float targetGain, final float fadeTime) {
        final float deltaGain = targetGain - this.getGain();
        if ((deltaGain > 0.0f && this.getMaxGain() > targetGain) || (deltaGain < 0.0f && this.getMaxGain() < targetGain)) {
            this.setMaxGain(targetGain);
        }
        if (fadeTime > 0.0f) {
            this.m_gainVariation = deltaGain * 10.0f / fadeTime;
        }
        else {
            this.setGain(targetGain);
        }
    }
    
    public void fadeOut(final float factor) {
        this.m_gainVariation = -factor;
    }
    
    public void setEndDate(final long endDate) {
        this.m_endDate = endDate;
    }
    
    public void setFadeOutDate(final long fadeOutDate) {
        this.m_fadeOutDate = fadeOutDate;
    }
    
    public void setRepeatCount(final int playCount) {
        this.m_playCount = playCount;
    }
    
    public void setStopOnNullGain(final boolean stopOnNullGain) {
        this.m_stopOnNullGain = stopOnNullGain;
    }
    
    public void setPauseOnNullGain(final boolean pauseOnNullGain) {
        this.m_pauseOnNullGain = pauseOnNullGain;
    }
    
    public void setReleaseOnNullGain(final boolean releaseOnNullGain) {
        this.m_releaseOnNullGain = releaseOnNullGain;
    }
    
    public boolean isStopOnNullGain() {
        return this.m_stopOnNullGain;
    }
    
    public boolean isPauseOnNullGain() {
        return this.m_pauseOnNullGain;
    }
    
    public boolean isReleaseOnNullGain() {
        return this.m_releaseOnNullGain;
    }
    
    public String getDescription() {
        if (this.m_stream == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("URL : ").append(this.m_stream.getDescription());
        return sb.toString();
    }
    
    public String getFileId() {
        if (this.m_stream == null) {
            return null;
        }
        return this.m_stream.getFileId();
    }
    
    public long getFileIdValue() {
        if (this.m_stream == null) {
            return -1L;
        }
        return PrimitiveConverter.getLong(this.m_stream.getFileId());
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("URL : ").append(this.m_stream.getDescription());
        sb.append("\n\tSample Rate : ").append(this.m_stream.getSampleRate()).append("Hz");
        sb.append("\n\tChannels : ").append(this.m_stream.getNumChannels());
        return sb.toString();
    }
    
    static {
        m_logger = Logger.getLogger((Class)AudioSource.class);
    }
}
