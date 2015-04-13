package com.ankamagames.framework.graphics.engine.states;

public enum TextureBlendModes
{
    Add(260), 
    Blend(3042), 
    Combine(34160), 
    Decal(8449), 
    Modulate(8448);
    
    public final int m_oglCode;
    
    private TextureBlendModes(final int oglCode) {
        this.m_oglCode = oglCode;
    }
}
