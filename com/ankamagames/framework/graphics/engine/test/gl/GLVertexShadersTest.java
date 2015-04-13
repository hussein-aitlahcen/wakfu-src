package com.ankamagames.framework.graphics.engine.test.gl;

import com.sun.opengl.cg.*;
import com.ankamagames.framework.graphics.engine.test.*;

public class GLVertexShadersTest extends AbstractGLFeatureTest
{
    @Override
    public boolean runTest() {
        final String extensions = this.m_gl.glGetString(7939);
        if (extensions == null || !extensions.contains("GL_ARB_vertex_shader")) {
            return false;
        }
        try {
            return CgGL.cgGLIsProfileSupported(6150);
        }
        catch (Throwable e) {
            return false;
        }
    }
    
    @Override
    public HardwareFeature getFeature() {
        return HardwareFeature.GL_VERTEX_SHADERS;
    }
}
