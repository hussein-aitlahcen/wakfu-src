package com.ankamagames.wakfu.client.core.news;

public class VideoQualityView
{
    private final int m_quality;
    
    public VideoQualityView(final int quality) {
        super();
        this.m_quality = quality;
    }
    
    public int getQuality() {
        return this.m_quality;
    }
    
    @Override
    public String toString() {
        return this.m_quality + "p";
    }
}
