package com.ankamagames.framework.graphics.engine.states;

public enum BlendModes
{
    Zero(0), 
    One(1), 
    SrcColor(768), 
    InvSrcColor(769), 
    SrcAlpha(770), 
    InvSrcAlpha(771), 
    DestColor(774), 
    InvDestColor(775), 
    DestAlpha(772), 
    InvDestAlpha(773), 
    SrcAlphaSaturate(776);
    
    public final int m_oglCode;
    
    private BlendModes(final int oglCode) {
        this.m_oglCode = oglCode;
    }
    
    public static BlendModes fromOGL(final int value) {
        for (final BlendModes blend : values()) {
            if (blend.m_oglCode == value) {
                return blend;
            }
        }
        return BlendModes.Zero;
    }
}
