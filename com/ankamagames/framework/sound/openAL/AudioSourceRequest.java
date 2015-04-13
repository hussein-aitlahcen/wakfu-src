package com.ankamagames.framework.sound.openAL;

import com.ankamagames.framework.sound.group.*;

public class AudioSourceRequest implements AudioSourceDefinition
{
    private long m_soundUID;
    private long m_soundId;
    private float m_gain;
    private int m_playCount;
    private long m_endDate;
    private long m_fadeOutDate;
    private int m_fightId;
    private ObservedSource m_observed;
    private int m_rollOffPresetsId;
    private boolean m_autoRelease;
    
    public AudioSourceRequest(final long soundUID, final long soundId, final float gain, final int playCount, final long endDate, final long fadeOutDate, final int fightId) {
        super();
        this.m_fightId = -1;
        this.m_soundUID = soundUID;
        this.m_soundId = soundId;
        this.m_gain = gain;
        this.m_playCount = playCount;
        this.m_endDate = endDate;
        this.m_fadeOutDate = fadeOutDate;
        this.m_fightId = fightId;
        this.m_observed = null;
        this.m_rollOffPresetsId = -1;
        this.m_autoRelease = true;
    }
    
    public AudioSourceRequest(final long soundUID, final long soundId, final float gain, final int playCount, final long endDate, final long fadeOutDate, final int fightId, final ObservedSource observed, final int rollOffPresetsId, final boolean autoRelease) {
        super();
        this.m_fightId = -1;
        this.m_soundUID = soundUID;
        this.m_soundId = soundId;
        this.m_gain = gain;
        this.m_playCount = playCount;
        this.m_endDate = endDate;
        this.m_fadeOutDate = fadeOutDate;
        this.m_fightId = fightId;
        this.m_observed = observed;
        this.m_rollOffPresetsId = rollOffPresetsId;
        this.m_autoRelease = autoRelease;
    }
    
    public long getSoundId() {
        return this.m_soundId;
    }
    
    public float getGain() {
        return this.m_gain;
    }
    
    public int getPlayCount() {
        return this.m_playCount;
    }
    
    public long getEndDate() {
        return this.m_endDate;
    }
    
    public long getFadeOutDate() {
        return this.m_fadeOutDate;
    }
    
    public int getFightId() {
        return this.m_fightId;
    }
    
    public ObservedSource getObserved() {
        return this.m_observed;
    }
    
    public int getRollOffPresetsId() {
        return this.m_rollOffPresetsId;
    }
    
    public boolean isAutoRelease() {
        return this.m_autoRelease;
    }
    
    public void setSoundUID(final long soundUID) {
        this.m_soundUID = soundUID;
    }
    
    @Override
    public long getSoundUID() {
        return this.m_soundUID;
    }
}
