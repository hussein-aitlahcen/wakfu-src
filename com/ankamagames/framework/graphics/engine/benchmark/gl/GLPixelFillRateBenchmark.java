package com.ankamagames.framework.graphics.engine.benchmark.gl;

import com.ankamagames.framework.graphics.engine.benchmark.*;
import com.ankamagames.framework.graphics.engine.*;
import java.nio.*;
import com.ankamagames.framework.graphics.engine.opengl.*;

public class GLPixelFillRateBenchmark extends AbstractGLFillRateBenchmark
{
    @Override
    public void run(final BenchmarkResult result) {
        final GLRenderer glRenderer = RendererType.OpenGL.getRenderer();
        this.m_gl.glDrawBuffer(1029);
        this.prepareViewport();
        glRenderer.m_stateManager.setVertexArrayComponents(5);
        final IndexBuffer indexBuffer = IndexBuffer.QUAD_INDICES;
        int glDrawCount = 0;
        final long start = System.nanoTime();
        while (System.nanoTime() - start < GLPixelFillRateBenchmark.DURATION) {
            this.m_gl.glVertexPointer(2, 5126, 0, (Buffer)this.m_vertexBuffer.getPositionBuffer());
            this.m_gl.glColorPointer(4, 5126, 0, (Buffer)this.m_vertexBuffer.getColorBuffer());
            this.m_gl.glDrawElements(7, 4, 5123, (Buffer)indexBuffer.getBuffer());
            ++glDrawCount;
        }
        final float fillRateIndex = glDrawCount / (GLPixelFillRateBenchmark.DURATION / 1.0E9f);
        GLPixelFillRateBenchmark.m_logger.info((Object)("colored quad draw/s ~= " + fillRateIndex));
        result.setPixelFillRateIndex(fillRateIndex);
    }
    
    @Override
    public String getName() {
        return "GL pixel fillrate";
    }
}
