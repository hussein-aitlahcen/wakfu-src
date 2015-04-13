package com.ankamagames.framework.sound.openAL;

import org.apache.log4j.*;
import gnu.trove.*;
import org.jetbrains.annotations.*;
import org.lwjgl.openal.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class LowPassFilter
{
    private static final Logger m_logger;
    private static final int DURATION = 500;
    private final SoundManager m_manager;
    private final int m_maskKey;
    private final int[] m_filterId;
    private final float m_targetGain;
    private final float m_targetGainHF;
    private final TLongArrayList m_registeredSoundUIDs;
    private float m_gain;
    private float m_gainHF;
    private boolean m_isVisible;
    private LowPassFilterTween m_tween;
    
    public LowPassFilter(final float targetGain, final float targetGainHF, final int maskKey, @NotNull final SoundManager manager, final boolean isVisibleFromCamera) {
        super();
        this.m_filterId = new int[] { 0 };
        this.m_registeredSoundUIDs = new TLongArrayList();
        this.m_manager = manager;
        this.m_maskKey = maskKey;
        this.m_isVisible = isVisibleFromCamera;
        this.m_targetGain = targetGain;
        this.m_targetGainHF = targetGainHF;
        this.m_gain = (this.m_isVisible ? 1.0f : targetGain);
        this.m_gainHF = (this.m_isVisible ? 1.0f : targetGainHF);
    }
    
    public void createFilter() {
        this.m_filterId[0] = EFX10.alGenFilters();
        final SoundManager manager = this.m_manager;
        SoundManager.check();
        EFX10.alFilteri(this.m_filterId[0], 32769, 1);
        final SoundManager manager2 = this.m_manager;
        SoundManager.check();
        this.setFilterValue(this.m_gain, this.m_gainHF);
    }
    
    public void deleteFilter() {
        if (this.m_filterId[0] != 0) {
            final SoundManager manager = this.m_manager;
            SoundManager.check();
            EFX10.alDeleteFilters(this.m_filterId[0]);
            final SoundManager manager2 = this.m_manager;
            SoundManager.check();
            this.m_filterId[0] = 0;
        }
    }
    
    public void setIsVisibleFromCamera(final boolean isVisible) {
        if (this.m_isVisible == isVisible) {
            return;
        }
        this.m_isVisible = isVisible;
        this.m_tween = (this.m_isVisible ? new LowPassFilterTween(1.0f, 1.0f, 500) : new LowPassFilterTween(this.m_targetGain, this.m_targetGainHF, 500));
    }
    
    public int getMaskKey() {
        return this.m_maskKey;
    }
    
    public int getFilterId() {
        return this.m_filterId[0];
    }
    
    public void process(final long currentTime) {
        if (this.m_tween != null && !this.m_tween.process(currentTime)) {
            this.m_tween = null;
        }
    }
    
    private void setFilterValue(final float gain, final float gainHF) {
        EFX10.alFilterf(this.m_filterId[0], 1, gain);
        final SoundManager manager = this.m_manager;
        SoundManager.check();
        EFX10.alFilterf(this.m_filterId[0], 2, gainHF);
        final SoundManager manager2 = this.m_manager;
        SoundManager.check();
        for (int i = this.m_registeredSoundUIDs.size() - 1; i >= 0; --i) {
            this.updateSound(this.m_registeredSoundUIDs.get(i));
        }
    }
    
    private void updateSound(final long soundUID) {
        final AudioSource source = AudioSourceManager.getInstance().getAudioSource(soundUID);
        if (source != null) {
            source.applyLowPassSetting(this.m_filterId[0]);
        }
    }
    
    public void addReference(final long soundUID) {
        if (!this.m_registeredSoundUIDs.contains(soundUID)) {
            this.m_registeredSoundUIDs.add(soundUID);
            this.updateSound(soundUID);
        }
    }
    
    public void removeReference(final long soundUID) {
        TroveUtils.removeFirstValue(this.m_registeredSoundUIDs, soundUID);
        if (this.m_registeredSoundUIDs.isEmpty()) {
            this.onZeroReference();
        }
    }
    
    private void onZeroReference() {
        if (this.m_manager != null) {
            this.m_manager.onLowPassFilterDelete(this.m_maskKey);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)LowPassFilter.class);
    }
    
    public class LowPassFilterTween
    {
        private final float m_previousGain;
        private final float m_previousGainHF;
        private final float m_newGain;
        private final float m_newGainHF;
        private final int m_duration;
        private long m_startDate;
        
        public LowPassFilterTween(final float newGain, final float newGainHF, final int duration) {
            super();
            this.m_startDate = -1L;
            this.m_previousGain = LowPassFilter.this.m_gain;
            this.m_previousGainHF = LowPassFilter.this.m_gainHF;
            this.m_newGain = newGain;
            this.m_newGainHF = newGainHF;
            this.m_duration = duration;
        }
        
        public boolean process(final long currentTime) {
            if (this.m_startDate == -1L) {
                this.m_startDate = currentTime;
                return true;
            }
            final int actualCurrentTime = (int)(currentTime - this.m_startDate);
            final int current = MathHelper.clamp(actualCurrentTime, 0, this.m_duration);
            LowPassFilter.this.m_gain = this.compute(this.m_previousGain, this.m_newGain, current, this.m_duration);
            LowPassFilter.this.m_gainHF = this.compute(this.m_previousGainHF, this.m_newGainHF, current, this.m_duration);
            LowPassFilter.this.setFilterValue(LowPassFilter.this.m_gain, LowPassFilter.this.m_gainHF);
            return actualCurrentTime < this.m_duration;
        }
        
        private float compute(final float a, final float b, final int current, final int duration) {
            float percent = current / duration;
            final float delta = (0.5f - percent) * (1.0f - 2.0f * Math.abs(0.5f - percent));
            percent -= delta;
            return a + (b - a) * percent;
        }
    }
    
    public interface LowPassFilterListener
    {
        void onLowPassFilterDelete(int p0);
    }
}
