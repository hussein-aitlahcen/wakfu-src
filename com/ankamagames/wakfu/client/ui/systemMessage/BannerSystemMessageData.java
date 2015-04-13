package com.ankamagames.wakfu.client.ui.systemMessage;

public class BannerSystemMessageData extends SystemMessageData
{
    private final String m_animationName;
    
    public BannerSystemMessageData(final WakfuSystemMessageManager.SystemMessageType type, final String message, final String animationName) {
        super(type, message);
        this.m_animationName = animationName;
    }
    
    public BannerSystemMessageData(final WakfuSystemMessageManager.SystemMessageType type, final String message, final int duration, final String animation) {
        super(type, message, duration);
        this.m_animationName = animation;
    }
    
    public BannerSystemMessageData(final WakfuSystemMessageManager.SystemMessageType type, final String message, final int fadeDuration, final int duration, final String animation) {
        super(type, message, fadeDuration, duration);
        this.m_animationName = animation;
    }
    
    public String getAnimationName() {
        return this.m_animationName;
    }
}
