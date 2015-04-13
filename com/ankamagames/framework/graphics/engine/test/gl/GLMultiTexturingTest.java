package com.ankamagames.framework.graphics.engine.test.gl;

import com.ankamagames.framework.graphics.engine.test.*;

public class GLMultiTexturingTest extends AbstractGLFeatureTest
{
    @Override
    public boolean runTest() {
        final String extensions = this.m_gl.glGetString(7939);
        return extensions != null && extensions.contains("GL_ARB_multitexture");
    }
    
    @Override
    public HardwareFeature getFeature() {
        return HardwareFeature.GL_MULTI_TEXTURING;
    }
}
