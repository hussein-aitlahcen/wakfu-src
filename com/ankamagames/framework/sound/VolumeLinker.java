package com.ankamagames.framework.sound;

public class VolumeLinker
{
    public static float DEFAULT_GAIN;
    public static float DEFAULT_FADEOUT;
    private float m_baseGain;
    private float m_targetGain;
    private float m_fadeOutTime;
    private GainModifiable m_source;
    
    public VolumeLinker(final GainModifiable source) {
        super();
        this.m_baseGain = 0.0f;
        this.m_targetGain = VolumeLinker.DEFAULT_GAIN;
        this.m_fadeOutTime = VolumeLinker.DEFAULT_FADEOUT;
        this.m_source = source;
    }
    
    public void onStart() {
        if (this.m_source == null) {
            return;
        }
        this.m_baseGain = this.m_source.getGain();
        this.m_source.fade(this.m_targetGain * this.m_source.getGain(), this.m_fadeOutTime);
    }
    
    public void onEnd() {
        if (this.m_source == null) {
            return;
        }
        this.m_source.fade(this.m_baseGain, this.m_fadeOutTime);
    }
    
    public void setTargetGain(final float targetGain) {
        this.m_targetGain = targetGain;
    }
    
    public void setFadeOutTime(final float fadeOutTime) {
        this.m_fadeOutTime = fadeOutTime;
    }
    
    public void resetTargetGain() {
        this.m_targetGain = VolumeLinker.DEFAULT_GAIN;
    }
    
    public void resetFadeOutTime() {
        this.m_fadeOutTime = VolumeLinker.DEFAULT_FADEOUT;
    }
    
    static {
        VolumeLinker.DEFAULT_GAIN = 0.4f;
        VolumeLinker.DEFAULT_FADEOUT = 700.0f;
    }
}
