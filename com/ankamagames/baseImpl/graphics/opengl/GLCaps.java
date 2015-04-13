package com.ankamagames.baseImpl.graphics.opengl;

public class GLCaps
{
    public boolean m_useDoubleBuffer;
    public int m_bpp;
    public int m_numDepthBufferBits;
    public int m_numStencilBits;
    
    public GLCaps() {
        super();
        this.m_useDoubleBuffer = true;
        this.m_bpp = 32;
        this.m_numDepthBufferBits = 24;
        this.m_numStencilBits = 8;
    }
}
