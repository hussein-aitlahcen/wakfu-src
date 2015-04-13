package com.ankamagames.wakfu.common.game.travel.infos;

public class TravelLoadingInfo
{
    private final String m_animationName;
    private final int m_minDuration;
    private final int m_fadeInDuration;
    private final int m_fadeOutDuration;
    
    public TravelLoadingInfo(final String animationName, final int minDuration, final int fadeInDuration, final int fadeOutDuration) {
        super();
        this.m_animationName = animationName;
        this.m_minDuration = minDuration;
        this.m_fadeInDuration = fadeInDuration;
        this.m_fadeOutDuration = fadeOutDuration;
    }
    
    public String getAnimationName() {
        return this.m_animationName;
    }
    
    public int getMinDuration() {
        return this.m_minDuration;
    }
    
    public int getFadeInDuration() {
        return this.m_fadeInDuration;
    }
    
    public int getFadeOutDuration() {
        return this.m_fadeOutDuration;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("TravelLoadingInfo");
        sb.append("{m_animationName='").append(this.m_animationName).append('\'');
        sb.append(", m_minDuration=").append(this.m_minDuration);
        sb.append(", m_fadeInDuration=").append(this.m_fadeInDuration);
        sb.append(", m_fadeOutDuration=").append(this.m_fadeOutDuration);
        sb.append('}');
        return sb.toString();
    }
}
