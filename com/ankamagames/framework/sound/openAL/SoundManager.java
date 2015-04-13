package com.ankamagames.framework.sound.openAL;

import org.apache.log4j.*;
import com.ankamagames.framework.sound.group.*;
import com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey.*;
import org.lwjgl.openal.*;
import java.nio.*;
import org.lwjgl.*;
import com.ankamagames.framework.sound.stream.*;
import java.io.*;
import com.ankamagames.framework.sound.openAL.soundLogger.*;
import java.util.*;
import com.ankamagames.framework.sound.*;
import com.ankamagames.framework.graphics.engine.test.*;
import com.ankamagames.framework.sound.group.effects.*;
import com.ankamagames.framework.sound.group.field.*;

public abstract class SoundManager extends Thread implements LowPassFilter.LowPassFilterListener
{
    private static SoundManager m_instance;
    protected static final Logger m_logger;
    protected static final boolean DEBUG_MODE = true;
    protected static final boolean DEBUG_CACHE = false;
    protected static final int MAX_COMPRESSED_SOUND_SIZE_FOR_CACHE = 131072;
    public static final long UPDATE_TIME = 10L;
    private boolean m_running;
    protected static int[] m_effects;
    protected static int[] m_effectSlots;
    private int m_currentReverbSetting;
    private int m_currentLowpassSettings;
    private LowPassTween m_currentLowPassTweenIn;
    private LowPassTween m_currentLowPassTweenOut;
    private final ArrayList<AudioSourceGroup> m_groups;
    private final Object m_groupsMutex;
    protected ObservedListener m_listener;
    private volatile boolean m_openALInitialized;
    private volatile boolean m_initializationFailed;
    private int m_hardwareVoicesAvailable;
    private int m_hardwareEffectSlotsAvailable;
    private int m_hardwareEffectsAvailable;
    private int m_hardwareVoicesInUse;
    protected IntObjectLightWeightMap<LowPassFilter> m_lowPassFilters;
    long totalTime;
    int count;
    private static int m_cacheHit;
    private static int m_cacheMiss;
    
    protected SoundManager() {
        super();
        this.m_running = false;
        this.m_currentReverbSetting = -1;
        this.m_currentLowpassSettings = -1;
        this.m_currentLowPassTweenIn = null;
        this.m_currentLowPassTweenOut = null;
        this.m_groups = new ArrayList<AudioSourceGroup>();
        this.m_groupsMutex = new Object();
        this.m_openALInitialized = false;
        this.m_initializationFailed = false;
        this.m_hardwareVoicesAvailable = 0;
        this.m_hardwareEffectSlotsAvailable = 0;
        this.m_hardwareEffectsAvailable = 0;
        this.m_hardwareVoicesInUse = 0;
        this.m_lowPassFilters = new IntObjectLightWeightMap<LowPassFilter>();
        this.totalTime = 0L;
        this.count = 0;
        setInstance(this);
    }
    
    protected static void setInstance(final SoundManager sm) {
        SoundManager.m_instance = sm;
    }
    
    public static SoundManager getInstance() {
        return SoundManager.m_instance;
    }
    
    public final boolean isOpenALInitialized() {
        return this.m_openALInitialized;
    }
    
    public final ObservedListener getListener() {
        return this.m_listener;
    }
    
    public void setListener(final ObservedListener listener) {
        this.m_listener = listener;
    }
    
    public void stopAndClean() {
        this.setRunning(false);
        try {
            this.join(500L);
        }
        catch (InterruptedException e) {
            SoundManager.m_logger.debug((Object)"Thread interrupted while waiting for SoundManager to stop", (Throwable)e);
        }
        try {
            if (AL.isCreated()) {
                SoundManager.m_logger.info((Object)"SoundManager thread not closed in maximum time. Destroying AL");
                AL.destroy();
            }
        }
        catch (OpenALException e2) {
            SoundManager.m_logger.error((Object)"Exception en detruisant OpenAL : ", (Throwable)e2);
        }
    }
    
    public final void setRunning(final boolean running) {
        this.m_running = running;
    }
    
    public final boolean isRunning() {
        return this.m_running;
    }
    
    public static void check() {
        final int error = AL10.alGetError();
        if (error != 0) {
            String errorMessage = "unknown error";
            switch (error) {
                case 40961: {
                    errorMessage = "AL_INVALID_NAME";
                    break;
                }
                case 40962: {
                    errorMessage = "AL_INVALID_ENUM";
                    break;
                }
                case 40963: {
                    errorMessage = "AL_INVALID_VALUE";
                    break;
                }
                case 40964: {
                    errorMessage = "AL_INVALID_OPERATION";
                    break;
                }
                case 40965: {
                    errorMessage = "AL_OUT_OF_MEMORY";
                    break;
                }
            }
            throw new OpenALException("OpenAL error : " + errorMessage);
        }
    }
    
    private void computeMaxHardwareVoices() {
        final IntBuffer buffer = BufferUtils.createIntBuffer(64);
        for (int i = 0; i < 64; ++i) {
            try {
                final int source = AL10.alGenSources();
                check();
                buffer.put(source);
            }
            catch (Exception e2) {
                break;
            }
        }
        this.m_hardwareVoicesAvailable = buffer.position();
        buffer.position(0).limit(this.m_hardwareVoicesAvailable);
        try {
            AL10.alDeleteSources(buffer);
            check();
        }
        catch (OpenALException e) {
            SoundManager.m_logger.warn((Object)"Probl\u00e8me au alDeleteSources.", (Throwable)e);
        }
        if (!ALC10.alcIsExtensionPresent(ALC10.alcGetContextsDevice(ALC10.alcGetCurrentContext()), "ALC_EXT_EFX")) {
            this.m_hardwareEffectsAvailable = 0;
            this.m_hardwareEffectSlotsAvailable = 0;
        }
        else {
            buffer.limit(64);
            for (int i = 0; i < 64; ++i) {
                try {
                    final int effect = EFX10.alGenEffects();
                    check();
                    buffer.put(effect);
                }
                catch (Exception e2) {
                    break;
                }
            }
            this.m_hardwareEffectsAvailable = buffer.position();
            buffer.position(0).limit(this.m_hardwareEffectsAvailable);
            try {
                EFX10.alDeleteEffects(buffer);
                check();
            }
            catch (OpenALException e) {
                SoundManager.m_logger.warn((Object)"Probl\u00e8me au alDeleteEffects.", (Throwable)e);
            }
            buffer.limit(64);
            for (int i = 0; i < 64; ++i) {
                try {
                    final int effectSlot = EFX10.alGenAuxiliaryEffectSlots();
                    check();
                    buffer.put(effectSlot);
                }
                catch (Exception e2) {
                    break;
                }
            }
            this.m_hardwareEffectSlotsAvailable = buffer.position();
            buffer.position(0).limit(this.m_hardwareEffectSlotsAvailable);
            try {
                EFX10.alDeleteAuxiliaryEffectSlots(buffer);
                check();
            }
            catch (OpenALException e) {
                SoundManager.m_logger.warn((Object)"Probl\u00e8me au alDeleteAuxiliaryEffectSlots.", (Throwable)e);
            }
        }
    }
    
    public final synchronized boolean initialize() {
        if (!this.m_openALInitialized) {
            try {
                AL.create();
                this.computeMaxHardwareVoices();
                AL10.alDistanceModel(53252);
                check();
                this.m_openALInitialized = true;
            }
            catch (LWJGLException e) {
                SoundManager.m_logger.warn((Object)"Probl?me lors de SoundManager.initialize(). Impossible d'initialiser le SoundManager", (Throwable)e);
                this.m_openALInitialized = false;
                this.m_initializationFailed = true;
                return false;
            }
        }
        return !this.m_running && this.onInitialize();
    }
    
    @Override
    public final synchronized void start() {
        if (!this.m_running && this.m_openALInitialized && !this.m_initializationFailed) {
            this.setName("SoundManager");
            super.start();
            while (!this.m_running) {
                try {
                    Thread.sleep(100L);
                }
                catch (InterruptedException e) {
                    SoundManager.m_logger.error((Object)"Interrupted", (Throwable)e);
                }
            }
        }
        else if (this.m_running) {
            SoundManager.m_logger.error((Object)"SoundManager is already running");
        }
        else if (this.m_initializationFailed) {
            SoundManager.m_logger.error((Object)"SoundManager failed to initialize");
        }
        else {
            SoundManager.m_logger.error((Object)"Initialize SoundManager first");
        }
    }
    
    protected void onUpdate(final long currentTime) {
    }
    
    public final void addWaitingSources() {
        synchronized (this.m_groupsMutex) {
            for (int groupsCount = this.m_groups.size(), i = 0; i < groupsCount; ++i) {
                this.m_groups.get(i).addWaitingSources();
            }
        }
    }
    
    @Override
    public final void run() {
        this.m_running = true;
        SoundManager.m_logger.info((Object)"SoundManager running");
        while (this.m_running) {
            try {
                Thread.sleep(10L);
            }
            catch (InterruptedException e) {
                SoundManager.m_logger.error((Object)"Exception", (Throwable)e);
            }
            final long currentTime = System.currentTimeMillis();
            try {
                this.onUpdate(currentTime);
            }
            catch (Throwable t) {
                SoundManager.m_logger.error((Object)"Exception", t);
            }
            synchronized (this.m_groupsMutex) {
                for (int groupsCount = this.m_groups.size(), i = 0; i < groupsCount; ++i) {
                    final AudioSourceGroup group = this.m_groups.get(i);
                    try {
                        if (!group.isMute() || group.canPrepareSoundsMuted()) {
                            group.update(currentTime);
                        }
                    }
                    catch (Throwable e2) {
                        SoundManager.m_logger.error((Object)"Exception", e2);
                    }
                }
            }
            for (int j = this.m_lowPassFilters.size() - 1; j >= 0; --j) {
                try {
                    this.m_lowPassFilters.getQuickValue(j).process(currentTime);
                }
                catch (Throwable t2) {
                    SoundManager.m_logger.error((Object)"Exception", t2);
                }
            }
        }
        try {
            this.onTerminate();
            AL.destroy();
        }
        catch (Throwable t3) {
            SoundManager.m_logger.error((Object)"Exception", t3);
        }
        SoundManager.m_logger.info((Object)"SoundManager stopped");
    }
    
    protected abstract boolean isSoundCacheActivated();
    
    private AudioStream getStream(final AudioStreamProvider asp) {
        if (!this.isSoundCacheActivated()) {
            final JOrbisStream stream = new JOrbisStream();
            stream.initialize(asp);
            return stream;
        }
        JOrbisSound sound = JOrbisSoundCache.INSTANCE.get(asp.getDescription());
        if (sound != null) {
            return new JOrbisVirtualStream(sound);
        }
        final JOrbisStream stream2 = new JOrbisStream();
        if (!stream2.initialize(asp)) {
            return null;
        }
        try {
            if (asp.length() < 131072L) {
                sound = new JOrbisSound(stream2);
                if (sound.initialize()) {
                    JOrbisSoundCache.INSTANCE.put(asp.getDescription(), sound);
                    return new JOrbisVirtualStream(sound);
                }
            }
        }
        catch (IOException e) {
            return null;
        }
        return stream2;
    }
    
    public final AudioSource prepareSound(final AudioStreamProvider asp, final AudioSourceGroup group, final long soundUID) {
        if (!this.m_openALInitialized) {
            try {
                asp.close();
            }
            catch (IOException e) {
                SoundManager.m_logger.error((Object)("Probl\u00e8me \u00e0 la fermeture " + asp.getDescription()));
            }
            return null;
        }
        final AudioStream stream = this.getStream(asp);
        Label_0227: {
            if (stream != null) {
                final AudioSource source = AudioSourceManager.getInstance().checkOut(soundUID);
                if (source != null) {
                    try {
                        if (source.initialize(this, group, stream)) {
                            stream.addRefCount();
                            stream.setSwap(true);
                            SoundLogger.log("Adding " + source.getFileId(), group.getParentId());
                            return source;
                        }
                        if (source.isSoundManagerRelease()) {
                            AudioSourceManager.getInstance().release(source);
                        }
                        stream.close();
                        return null;
                    }
                    catch (Exception ex) {
                        SoundManager.m_logger.error((Object)("Erreur durant l'initialisation de la source : " + asp.getDescription()));
                        if (source.isSoundManagerRelease()) {
                            AudioSourceManager.getInstance().release(source);
                        }
                        stream.subRefCount();
                        break Label_0227;
                    }
                }
                SoundManager.m_logger.error((Object)"error : source is null");
            }
            try {
                asp.close();
            }
            catch (IOException e2) {
                SoundManager.m_logger.error((Object)("Probl\u00e8me \u00e0 la fermeture du stream de " + asp.getDescription()));
            }
        }
        return null;
    }
    
    public static void stopAndReleaseSound(final AudioSource source) {
        if (source != null) {
            if (source.isPlaying()) {
                source.stop();
            }
            source.close();
            if (source.isSoundManagerRelease()) {
                AudioSourceManager.getInstance().release(source);
            }
        }
    }
    
    public boolean checkOutVoices(final int numVoices) {
        if (numVoices > this.m_hardwareVoicesAvailable - this.m_hardwareVoicesInUse) {
            return false;
        }
        this.m_hardwareVoicesInUse += numVoices;
        SoundManager.m_logger.debug((Object)("voices in use : " + this.m_hardwareVoicesInUse + "/" + this.m_hardwareVoicesAvailable));
        return true;
    }
    
    public boolean checkInVoices(final int numVoices) {
        if (this.m_hardwareVoicesInUse < numVoices) {
            return false;
        }
        this.m_hardwareVoicesInUse -= numVoices;
        SoundManager.m_logger.debug((Object)("voices in use : " + this.m_hardwareVoicesInUse + "/" + this.m_hardwareVoicesAvailable));
        return true;
    }
    
    protected int getVoicesInUse() {
        return this.m_hardwareVoicesInUse;
    }
    
    public final void addGroup(final AudioSourceGroup group) {
        if (group == null) {
            return;
        }
        synchronized (this.m_groupsMutex) {
            if (!this.m_groups.contains(group)) {
                this.m_groups.add(group);
                group.setManager(this);
            }
        }
    }
    
    public final void removeGroup(final AudioSourceGroup group) {
        if (group == null) {
            return;
        }
        synchronized (this.m_groupsMutex) {
            try {
                this.m_groups.remove(group);
                group.stop();
                group.setManager(null);
            }
            catch (Exception e) {
                SoundManager.m_logger.error((Object)"Exception", (Throwable)e);
            }
        }
    }
    
    public final AudioSourceGroup getGroupByName(final String name) {
        if (name == null) {
            return null;
        }
        synchronized (this.m_groupsMutex) {
            for (final AudioSourceGroup group : this.m_groups) {
                if (group.getName().equals(name)) {
                    return group;
                }
            }
        }
        return null;
    }
    
    protected abstract boolean onInitialize();
    
    protected abstract void onTerminate();
    
    public static int getAuxiliaryEffect() {
        return SoundManager.m_effectSlots[0];
    }
    
    public abstract PositionedSound addWorldSound(final IAmbienceSound p0, final int p1, final int p2, final int p3);
    
    public synchronized void setReverbFromParams(final int reverbPresetId) throws OpenALException {
        if (!this.isRunning()) {
            return;
        }
        if (!HardwareFeatureManager.INSTANCE.isFeatureSupported(HardwareFeature.AL_EFFECT)) {
            return;
        }
        if (SoundManager.m_effectSlots[0] == 0) {
            SoundManager.m_effectSlots[0] = EFX10.alGenAuxiliaryEffectSlots();
            check();
        }
        if (SoundManager.m_effects[0] == 0) {
            SoundManager.m_effects[0] = EFX10.alGenEffects();
            check();
        }
        if (this.m_currentReverbSetting == reverbPresetId) {
            return;
        }
        this.m_currentReverbSetting = reverbPresetId;
        final ReverbParameter params = this.getReverbParameter(this.m_currentReverbSetting);
        if (params != null) {
            try {
                EFX10.alEffecti(SoundManager.m_effects[0], 32769, 1);
                check();
                EFX10.alEffectf(SoundManager.m_effects[0], 1, params.getReverbDensity());
                check();
                EFX10.alEffectf(SoundManager.m_effects[0], 3, params.getReverbGain());
                check();
                EFX10.alEffectf(SoundManager.m_effects[0], 5, params.getReverbDecayTime());
                check();
                EFX10.alEffectf(SoundManager.m_effects[0], 6, params.getDecayLPF());
                check();
                EFX10.alEffectf(SoundManager.m_effects[0], 9, params.getEarlyGain());
                check();
                EFX10.alEffectf(SoundManager.m_effects[0], 10, params.getPreDelay());
                check();
                EFX10.alAuxiliaryEffectSloti(SoundManager.m_effectSlots[0], 1, SoundManager.m_effects[0]);
                check();
                this.applyReverb(SoundManager.m_effectSlots[0]);
            }
            catch (Exception e) {
                this.applyReverb(0);
            }
        }
        else {
            this.applyReverb(0);
        }
    }
    
    public void setLowPassFilter(final int lowpassSetting) {
        if (!this.isRunning()) {
            return;
        }
        if (this.m_currentLowpassSettings == lowpassSetting) {
            return;
        }
        this.m_currentLowpassSettings = lowpassSetting;
    }
    
    public void registerToLowPassFilter(final int maskKey, final long soundUID) {
        if (!HardwareFeatureManager.INSTANCE.isFeatureSupported(HardwareFeature.AL_FILTER)) {
            return;
        }
        LowPassFilter filter = this.m_lowPassFilters.get(maskKey);
        if (filter == null) {
            final LowPassParameter params = this.getLowPassParameter(this.m_currentLowpassSettings);
            if (params == null) {
                return;
            }
            filter = new LowPassFilter(params.getGain(), params.getGainHF(), maskKey, this, this.isVisibleFromCamera(maskKey));
            try {
                filter.createFilter();
            }
            catch (OpenALException e) {
                return;
            }
            this.m_lowPassFilters.put(maskKey, filter);
        }
        filter.addReference(soundUID);
    }
    
    protected boolean isVisibleFromCamera(final int maskKey) {
        return true;
    }
    
    public void unregisterToLowPassFilter(final int maskKey, final long soundUID) {
        if (!HardwareFeatureManager.INSTANCE.isFeatureSupported(HardwareFeature.AL_FILTER)) {
            return;
        }
        final LowPassFilter filter = this.m_lowPassFilters.get(maskKey);
        if (filter != null) {
            filter.removeReference(soundUID);
        }
    }
    
    @Override
    public void onLowPassFilterDelete(final int maskKey) {
        final LowPassFilter passFilter = this.m_lowPassFilters.remove(maskKey);
        if (passFilter != null) {
            passFilter.deleteFilter();
        }
    }
    
    public abstract RollOffParameter getRollOffParameter(final int p0);
    
    protected abstract LowPassParameter getLowPassParameter(final int p0);
    
    protected abstract ReverbParameter getReverbParameter(final int p0);
    
    protected abstract void applyReverb(final int p0);
    
    public void setAreFiltersSupported(final boolean supported) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)SoundManager.class);
        SoundManager.m_effects = new int[] { 0 };
        SoundManager.m_effectSlots = new int[] { 0 };
        SoundManager.m_cacheHit = 0;
        SoundManager.m_cacheMiss = 0;
    }
}
