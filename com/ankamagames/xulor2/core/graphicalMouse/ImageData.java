package com.ankamagames.xulor2.core.graphicalMouse;

public class ImageData implements GraphicalMouseData
{
    public static final byte DEFAULT_MOUSE_DATA_TYPE = 1;
    private String m_iconUrl;
    
    public ImageData(final String iconUrl) {
        super();
        this.m_iconUrl = iconUrl;
    }
    
    public String getIconUrl() {
        return this.m_iconUrl;
    }
    
    public void setIconUrl(final String iconUrl) {
        this.m_iconUrl = iconUrl;
    }
    
    @Override
    public byte getType() {
        return 1;
    }
}
