package com.ankamagames.framework.sound.group;

import com.ankamagames.framework.sound.*;
import org.apache.log4j.*;
import com.ankamagames.framework.sound.helper.*;
import java.util.*;
import com.ankamagames.framework.sound.openAL.*;
import com.ankamagames.framework.sound.stream.*;
import java.io.*;

public abstract class AudioSourceGroup implements GainModifiable
{
    private static final boolean DEBUG = false;
    protected static final Logger m_logger;
    private byte m_hardwareVoicesAvailable;
    private byte m_hardwareVoicesInUse;
    private float m_defaultGainMod;
    private float m_gainMod;
    private float m_gain;
    private float m_maxGain;
    private float m_gainVariation;
    private float m_targetGain;
    private boolean m_mute;
    private boolean m_canPrepareSoundsMuted;
    private final String m_name;
    protected ObservedListener m_listener;
    protected SoundManager m_manager;
    protected AudioResourceHelper m_helper;
    protected int m_currentEffectSlot;
    protected boolean m_enableLowPassFilter;
    protected byte m_priority;
    private boolean m_enabled;
    private final ArrayList<AudioSourceGroupUpdateListener> m_updateListeners;
    private final ArrayList<AudioSourceGroupUpdateListener> m_updateListenersToAdd;
    private final ArrayList<AudioSourceGroupUpdateListener> m_updateListenersToRemove;
    private byte m_parentId;
    
    protected AudioSourceGroup(final String name) {
        this(name, (byte)(-1));
    }
    
    protected AudioSourceGroup(final String name, final byte parentId) {
        this(name, (byte)0, parentId);
    }
    
    protected AudioSourceGroup(final String name, final byte priority, final byte parentId) {
        super();
        this.m_hardwareVoicesAvailable = 0;
        this.m_hardwareVoicesInUse = 0;
        this.m_currentEffectSlot = 0;
        this.m_priority = 0;
        this.m_enabled = true;
        this.m_updateListeners = new ArrayList<AudioSourceGroupUpdateListener>();
        this.m_updateListenersToAdd = new ArrayList<AudioSourceGroupUpdateListener>();
        this.m_updateListenersToRemove = new ArrayList<AudioSourceGroupUpdateListener>();
        this.m_gain = 1.0f;
        this.m_gainMod = 1.0f;
        this.m_defaultGainMod = 1.0f;
        this.m_maxGain = 1.0f;
        this.m_mute = false;
        this.m_canPrepareSoundsMuted = false;
        this.m_name = name;
        this.m_listener = null;
        this.m_gainVariation = 0.0f;
        this.m_priority = priority;
        this.m_parentId = parentId;
    }
    
    public boolean checkOutVoices(final int numVoices) {
        if (numVoices > this.m_hardwareVoicesAvailable - this.m_hardwareVoicesInUse) {
            return false;
        }
        this.m_hardwareVoicesInUse += (byte)numVoices;
        return true;
    }
    
    public boolean checkInVoices(final int numVoices) {
        if (this.m_hardwareVoicesInUse < numVoices) {
            return false;
        }
        this.m_hardwareVoicesInUse -= (byte)numVoices;
        return true;
    }
    
    public void addUpdateListener(final AudioSourceGroupUpdateListener l) {
        this.m_updateListenersToAdd.add(l);
    }
    
    public void removeUpdateListener(final AudioSourceGroupUpdateListener l) {
        this.m_updateListenersToRemove.add(l);
    }
    
    public byte getHardwareVoicesAvailable() {
        return this.m_hardwareVoicesAvailable;
    }
    
    public boolean setHardwareVoicesAvailable(final byte hardwareVoicesAvailable) {
        assert hardwareVoicesAvailable >= 0 : "Nombre de voix invalide";
        if (hardwareVoicesAvailable == this.m_hardwareVoicesAvailable) {
            return false;
        }
        if (hardwareVoicesAvailable > this.m_hardwareVoicesAvailable) {
            if (!this.m_manager.checkOutVoices(hardwareVoicesAvailable - this.m_hardwareVoicesAvailable)) {
                return false;
            }
        }
        else if (!this.m_manager.checkInVoices(this.m_hardwareVoicesAvailable - hardwareVoicesAvailable)) {
            return false;
        }
        this.m_hardwareVoicesAvailable = hardwareVoicesAvailable;
        return true;
    }
    
    public byte getParentId() {
        return this.m_parentId;
    }
    
    public byte getPriority() {
        return this.m_priority;
    }
    
    public void setPriority(final byte priority) {
        this.m_priority = priority;
    }
    
    public boolean isEnabled() {
        return this.m_enabled;
    }
    
    public void setEnabled(final boolean enabled) {
        this.m_enabled = enabled;
    }
    
    public ObservedListener getListener() {
        return this.m_listener;
    }
    
    public void setListener(final ObservedListener listener) {
        this.m_listener = listener;
    }
    
    public void setManager(final SoundManager manager) {
        this.m_manager = manager;
    }
    
    public SoundManager getManager() {
        return this.m_manager;
    }
    
    public final void setHelper(final AudioResourceHelper helper) {
        this.m_helper = helper;
    }
    
    public final AudioResourceHelper getHelper() {
        assert this.m_helper != null : "Il faut d'abord initialiser en  appelant setHelper";
        return this.m_helper;
    }
    
    public float getGainMod() {
        return this.m_gainMod;
    }
    
    public void setGainMod(final float gainMod) {
        this.onGainModChanged(this.m_gainMod = gainMod);
    }
    
    public float getDefaultGainMod() {
        return this.m_defaultGainMod;
    }
    
    public void setDefaultGainMod(final float defaultGainMod) {
        this.setGainMod(this.m_defaultGainMod = defaultGainMod);
    }
    
    public void setGainModRatioToDefault(final float ratio) {
        this.setGainMod(this.m_defaultGainMod * ratio);
    }
    
    public float getGainModRatioToDefault() {
        return this.m_gainMod / this.m_defaultGainMod;
    }
    
    @Override
    public final float getGain() {
        return this.m_gain * this.m_gainMod;
    }
    
    public final float getInnerGain() {
        return this.m_gain;
    }
    
    public final void setGain(final float gain) {
        if (this.m_gain != gain) {
            final float oldGain = this.m_gain;
            this.onGainChanged(oldGain, this.m_gain = gain);
            this.onGainModChanged(this.getGainMod());
        }
    }
    
    public final void setMaxGain(final float maxGain) {
        if (this.m_maxGain != maxGain) {
            this.onMaxGainChanged(this.m_maxGain, maxGain);
            this.onGainModChanged(this.getGainMod());
            this.m_maxGain = maxGain;
        }
    }
    
    public final float getMaxGain() {
        return this.m_maxGain;
    }
    
    public final boolean isMute() {
        return this.m_mute;
    }
    
    public final void setMute(final boolean mute) {
        this.onMuteChanged(this.m_mute, mute);
        this.m_mute = mute;
    }
    
    public boolean canPrepareSounds() {
        return this.m_enabled && (!this.m_mute || this.m_canPrepareSoundsMuted);
    }
    
    public boolean canPrepareSoundsMuted() {
        return this.m_canPrepareSoundsMuted;
    }
    
    public void setCanPrepareSoundsMuted(final boolean canPrepareSoundsMuted) {
        this.m_canPrepareSoundsMuted = canPrepareSoundsMuted;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public abstract Collection<AudioSource> getSources();
    
    public abstract AudioSource addSource(final AudioStreamProvider p0, final boolean p1, final boolean p2, final boolean p3, final long p4);
    
    public AudioSource addSource(final long id, final boolean bStreaming, final boolean bStereo, final boolean bLoop, final long soundUID) {
        if (this.m_helper == null || !this.canPrepareSounds()) {
            return null;
        }
        AudioStreamProvider asp;
        try {
            asp = this.m_helper.fromId(id);
        }
        catch (IOException e) {
            AudioSourceGroup.m_logger.error((Object)("Impossible de charger le son d'id " + id));
            return null;
        }
        if (asp == null) {
            AudioSourceGroup.m_logger.error((Object)("Impossible de charger le son d'id " + id));
            return null;
        }
        return this.addSource(asp, bStreaming, bStereo, bLoop, soundUID);
    }
    
    public abstract void addSource(final AudioSource p0);
    
    public abstract void onGainChanged(final float p0, final float p1);
    
    public abstract void onGainModChanged(final float p0);
    
    public abstract void onMaxGainChanged(final float p0, final float p1);
    
    public abstract void onMuteChanged(final boolean p0, final boolean p1);
    
    public AudioSource prepareSound(final long id, final long soundUID) {
        AudioStreamProvider asp;
        try {
            asp = this.m_helper.fromId(id);
        }
        catch (IOException e) {
            AudioSourceGroup.m_logger.error((Object)("Impossible de pr\u00e9parer le son d'id " + id), (Throwable)e);
            return null;
        }
        if (asp == null) {
            AudioSourceGroup.m_logger.error((Object)("Impossible de pr\u00e9parer le son d'id " + id));
            return null;
        }
        return this.prepareSound(asp, soundUID);
    }
    
    public AudioSource prepareSound(final AudioStreamProvider url, final long soundUID) {
        if (this.canPrepareSounds()) {
            return this.m_manager.prepareSound(url, this, soundUID);
        }
        return null;
    }
    
    public abstract void addWaitingSources();
    
    public void update(final long currentTime) throws Exception {
        for (int i = this.m_updateListenersToAdd.size() - 1; i >= 0; --i) {
            this.m_updateListeners.add(this.m_updateListenersToAdd.remove(i));
        }
        for (int i = this.m_updateListenersToRemove.size() - 1; i >= 0; --i) {
            this.m_updateListeners.remove(this.m_updateListenersToRemove.remove(i));
        }
        for (int i = 0, size = this.m_updateListeners.size(); i < size; ++i) {
            this.m_updateListeners.get(i).beforeUpdate(this, currentTime);
        }
        float gain = 0.0f;
        if (this.m_gainVariation > 0.0f) {
            gain = Math.min(this.m_targetGain, this.m_gain + this.m_gainVariation);
        }
        else {
            if (this.m_gainVariation >= 0.0f) {
                return;
            }
            gain = Math.max(this.m_targetGain, this.m_gain + this.m_gainVariation);
        }
        if (gain == this.m_targetGain) {
            this.m_gainVariation = 0.0f;
            this.m_targetGain = 0.0f;
        }
        this.setGain(gain);
    }
    
    @Override
    public void fade(float targetGain, final float fadeTime) {
        if (fadeTime > 0.0f) {
            if (targetGain < 0.0f) {
                targetGain = 0.0f;
            }
            else if (targetGain > this.getMaxGain()) {
                this.setMaxGain(targetGain);
            }
            this.m_targetGain = targetGain;
            this.m_gainVariation = (targetGain - this.getGain()) * 10.0f / fadeTime;
        }
    }
    
    public abstract void stop() throws Exception;
    
    public abstract void stopSource(final AudioSource p0);
    
    public void pause() {
    }
    
    public void restart() {
    }
    
    public boolean applyReverb(final int effectSlot) {
        if (this.m_currentEffectSlot == effectSlot) {
            return false;
        }
        this.m_currentEffectSlot = effectSlot;
        return true;
    }
    
    public boolean isEnableLowPassFilter() {
        return this.m_enableLowPassFilter;
    }
    
    public void setEnableLowPassFilter(final boolean enableLowPassFilter) {
        this.m_enableLowPassFilter = enableLowPassFilter;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AudioSourceGroup.class);
    }
}
