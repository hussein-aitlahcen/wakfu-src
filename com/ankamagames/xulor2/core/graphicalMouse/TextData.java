package com.ankamagames.xulor2.core.graphicalMouse;

public class TextData implements GraphicalMouseData
{
    public static final byte TEXT_DATA_TYPE = 2;
    private String m_text;
    
    public TextData(final String iconUrl) {
        super();
        this.m_text = iconUrl;
    }
    
    public String getText() {
        return this.m_text;
    }
    
    public void setText(final String text) {
        this.m_text = text;
    }
    
    @Override
    public byte getType() {
        return 2;
    }
}
