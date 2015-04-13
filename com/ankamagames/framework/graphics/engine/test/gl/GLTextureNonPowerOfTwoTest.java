package com.ankamagames.framework.graphics.engine.test.gl;

import com.ankamagames.framework.graphics.engine.test.*;

public class GLTextureNonPowerOfTwoTest extends AbstractGLFeatureTest
{
    @Override
    public boolean runTest() {
        final String extensions = this.m_gl.glGetString(7939);
        return extensions != null && extensions.contains("GL_ARB_texture_non_power_of_two");
    }
    
    @Override
    public HardwareFeature getFeature() {
        return HardwareFeature.GL_TEXTURE_NON_POWER_OF_TWO;
    }
}
