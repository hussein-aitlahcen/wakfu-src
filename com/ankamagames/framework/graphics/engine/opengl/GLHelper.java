package com.ankamagames.framework.graphics.engine.opengl;

import javax.media.opengl.*;
import com.ankamagames.framework.graphics.engine.*;
import javax.media.opengl.glu.*;

public final class GLHelper
{
    public static GL getGL(final Renderer renderer) {
        assert renderer.getType() == RendererType.OpenGL;
        assert renderer.getDevice() != null;
        return ((GLRenderer)renderer).getDevice();
    }
    
    public static GLU getGLU(final Renderer renderer) {
        assert renderer.getType() == RendererType.OpenGL;
        return GLRenderer.m_glu;
    }
}
