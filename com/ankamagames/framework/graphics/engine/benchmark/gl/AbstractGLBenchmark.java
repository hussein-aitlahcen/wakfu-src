package com.ankamagames.framework.graphics.engine.benchmark.gl;

import com.ankamagames.framework.graphics.engine.benchmark.*;
import javax.media.opengl.*;
import com.ankamagames.framework.graphics.engine.*;

public abstract class AbstractGLBenchmark implements Benchmark
{
    protected GL m_gl;
    
    @Override
    public void initialize() {
        this.m_gl = RendererType.OpenGL.getRenderer().getDevice();
    }
    
    @Override
    public void cleanUp() {
        this.m_gl = null;
    }
}
