package com.ankamagames.framework.graphics.engine.test.gl;

import com.ankamagames.framework.graphics.engine.test.*;
import org.apache.log4j.*;
import javax.media.opengl.*;
import com.ankamagames.framework.graphics.engine.*;

public abstract class AbstractGLFeatureTest implements HardwareFeatureTest
{
    protected static final Logger m_logger;
    protected GL m_gl;
    
    @Override
    public void initialize() {
        this.m_gl = RendererType.OpenGL.getRenderer().getDevice();
    }
    
    @Override
    public void cleanUp() {
        this.m_gl = null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractGLFeatureTest.class);
        try {
            System.loadLibrary("Cg");
            System.loadLibrary("CgGL");
        }
        catch (Throwable t) {}
    }
}
