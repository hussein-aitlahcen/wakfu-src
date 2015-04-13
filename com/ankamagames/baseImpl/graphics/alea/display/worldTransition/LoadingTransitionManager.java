package com.ankamagames.baseImpl.graphics.alea.display.worldTransition;

import org.apache.log4j.*;

public class LoadingTransitionManager
{
    private static final Logger m_logger;
    private int m_fadeInDuration;
    private int m_fadeOutDuration;
    private int m_minTransitionDuration;
    private String m_anmName;
    private String m_videoName;
    private int m_soundId;
    private static final LoadingTransitionManager m_instance;
    
    public static LoadingTransitionManager getInstance() {
        return LoadingTransitionManager.m_instance;
    }
    
    private LoadingTransitionManager() {
        super();
        this.reset();
    }
    
    public int getFadeInDuration() {
        return this.m_fadeInDuration;
    }
    
    public int getFadeOutDuration() {
        return this.m_fadeOutDuration;
    }
    
    public int getMinTransitionDuration() {
        return this.m_minTransitionDuration;
    }
    
    public String getAnmName() {
        return this.m_anmName;
    }
    
    public int getSoundId() {
        return this.m_soundId;
    }
    
    public void setFadeInDuration(final int fadeInDuration) {
        this.m_fadeInDuration = fadeInDuration;
    }
    
    public void setFadeOutDuration(final int fadeOutDuration) {
        this.m_fadeOutDuration = fadeOutDuration;
    }
    
    public void setMinTransitionDuration(final int minTransitionDuration) {
        this.m_minTransitionDuration = minTransitionDuration;
    }
    
    public void setAnmName(final String anmName) {
        this.m_anmName = anmName;
    }
    
    public void setSoundId(final int soundId) {
        this.m_soundId = soundId;
    }
    
    public String getVideoName() {
        return this.m_videoName;
    }
    
    public void setVideoName(final String videoName) {
        this.m_videoName = videoName;
    }
    
    public void reset() {
        this.m_fadeInDuration = 1000;
        this.m_fadeOutDuration = 1000;
        this.m_minTransitionDuration = 0;
        this.m_anmName = null;
        this.m_videoName = null;
        this.m_soundId = 0;
    }
    
    static {
        m_logger = Logger.getLogger((Class)LoadingTransitionManager.class);
        m_instance = new LoadingTransitionManager();
    }
}
