package com.ankamagames.xulor2.core.graphicalMouse;

public class ImageTextData implements GraphicalMouseData
{
    public static final byte IMAGE_TEXT_DATA_TYPE = 3;
    private String m_iconUrl;
    private String m_text;
    
    public ImageTextData(final String iconUrl, final String text) {
        super();
        this.m_iconUrl = iconUrl;
        this.m_text = text;
    }
    
    public String getIconUrl() {
        return this.m_iconUrl;
    }
    
    public void setIconUrl(final String iconUrl) {
        this.m_iconUrl = iconUrl;
    }
    
    public String getText() {
        return this.m_text;
    }
    
    public void setText(final String text) {
        this.m_text = text;
    }
    
    @Override
    public byte getType() {
        return 3;
    }
}
