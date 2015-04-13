package com.ankamagames.wakfu.client.ui.systemMessage;

public class SystemMessageData
{
    private static final int FADE_DURATION_CONSTANT = 300;
    private WakfuSystemMessageManager.SystemMessageType m_type;
    private String m_message;
    private int m_fadeDuration;
    private int m_duration;
    
    public SystemMessageData(final WakfuSystemMessageManager.SystemMessageType type, final String message) {
        super();
        this.m_fadeDuration = 300;
        this.m_duration = Integer.MAX_VALUE;
        this.m_type = type;
        this.m_message = message;
    }
    
    public SystemMessageData(final WakfuSystemMessageManager.SystemMessageType type, final String message, final int duration) {
        super();
        this.m_fadeDuration = 300;
        this.m_duration = Integer.MAX_VALUE;
        this.m_type = type;
        this.m_message = message;
        this.m_duration = duration;
    }
    
    public SystemMessageData(final WakfuSystemMessageManager.SystemMessageType type, final String message, final int fadeDuration, final int duration) {
        super();
        this.m_fadeDuration = 300;
        this.m_duration = Integer.MAX_VALUE;
        this.m_type = type;
        this.m_message = message;
        this.m_fadeDuration = fadeDuration;
        this.m_duration = duration;
    }
    
    public int getFadeDuration() {
        return this.m_fadeDuration;
    }
    
    public int getDuration() {
        return this.m_duration;
    }
    
    public WakfuSystemMessageManager.SystemMessageType getType() {
        return this.m_type;
    }
    
    public String getMessage() {
        return this.m_message;
    }
    
    public void setFadeDuration(final int fadeDuration) {
        this.m_fadeDuration = fadeDuration;
    }
    
    public void setDuration(final int duration) {
        this.m_duration = duration;
    }
}
