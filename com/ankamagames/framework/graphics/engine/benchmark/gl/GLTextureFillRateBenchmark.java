package com.ankamagames.framework.graphics.engine.benchmark.gl;

import com.ankamagames.framework.graphics.engine.benchmark.*;
import com.ankamagames.framework.graphics.engine.*;
import java.nio.*;
import com.ankamagames.framework.graphics.engine.opengl.*;

public class GLTextureFillRateBenchmark extends AbstractGLFillRateBenchmark
{
    private GLTexture m_texture;
    
    @Override
    public void initialize() {
        super.initialize();
        final String texturePath = Engine.getInstance().getEffectPath() + "textures/cloud.tga";
        this.m_texture = new GLTexture(Engine.getTextureName(texturePath), texturePath, false);
    }
    
    @Override
    public void run(final BenchmarkResult result) {
        final GLRenderer glRenderer = RendererType.OpenGL.getRenderer();
        this.m_gl.glDrawBuffer(1029);
        this.prepareViewport();
        this.m_texture.activate(glRenderer);
        glRenderer.m_stateManager.setVertexArrayComponents(9);
        final IndexBuffer indexBuffer = IndexBuffer.QUAD_INDICES;
        int glDrawCount = 0;
        final long start = System.nanoTime();
        while (System.nanoTime() - start < GLTextureFillRateBenchmark.DURATION) {
            this.m_gl.glVertexPointer(2, 5126, 0, (Buffer)this.m_vertexBuffer.getPositionBuffer());
            this.m_gl.glTexCoordPointer(2, 5126, 0, (Buffer)this.m_vertexBuffer.getTexCoord0Buffer());
            this.m_gl.glDrawElements(7, 4, 5123, (Buffer)indexBuffer.getBuffer());
            ++glDrawCount;
        }
        final float fillRateIndex = glDrawCount / (GLTextureFillRateBenchmark.DURATION / 1.0E9f);
        GLTextureFillRateBenchmark.m_logger.info((Object)("textured quad draw/s ~= " + fillRateIndex));
        result.setTextureFillRateIndex(fillRateIndex);
    }
    
    @Override
    public void cleanUp() {
        final Renderer renderer = RendererType.OpenGL.getRenderer();
        this.m_texture.deactivate(renderer);
        this.m_texture.removeReference();
        super.cleanUp();
    }
    
    @Override
    public String getName() {
        return "GL texture fillrate";
    }
}
