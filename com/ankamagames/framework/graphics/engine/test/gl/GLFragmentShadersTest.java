package com.ankamagames.framework.graphics.engine.test.gl;

import com.ankamagames.framework.graphics.engine.fx.*;
import com.sun.opengl.cg.*;
import com.ankamagames.framework.graphics.engine.test.*;

public class GLFragmentShadersTest extends AbstractGLFeatureTest
{
    @Override
    public boolean runTest() {
        EffectManager.getInstance().setBaseEffect("transform");
        EffectManager.getInstance().setUiEffect("gui");
        final String extensions = this.m_gl.glGetString(7939);
        if (extensions == null || !extensions.contains("GL_ARB_fragment_shader")) {
            return false;
        }
        try {
            return CgGL.cgGLIsProfileSupported(7000);
        }
        catch (Throwable e) {
            return false;
        }
    }
    
    @Override
    public HardwareFeature getFeature() {
        return HardwareFeature.GL_FRAGMENT_SHADERS;
    }
}
