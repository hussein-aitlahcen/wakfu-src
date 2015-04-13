package com.ankamagames.framework.graphics.engine;

import com.ankamagames.framework.graphics.engine.opengl.*;

public enum RendererType
{
    OpenGL;
    
    private GLRenderer m_renderer;
    
    private RendererType(final int ordinal) {
        this.m_renderer = new GLRenderer();
    }
    
    public GLRenderer getRenderer() {
        return this.m_renderer;
    }
}
