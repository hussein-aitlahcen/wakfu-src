package com.ankamagames.framework.graphics.engine.test.gl;

import com.ankamagames.framework.graphics.engine.test.*;

public class GLRenderTargetTest extends AbstractGLFeatureTest
{
    @Override
    public boolean runTest() {
        final String extensions = this.m_gl.glGetString(7939);
        return extensions != null && extensions.contains("GL_EXT_framebuffer_object");
    }
    
    @Override
    public HardwareFeature getFeature() {
        return HardwareFeature.GL_RENDER_TARGET;
    }
}
