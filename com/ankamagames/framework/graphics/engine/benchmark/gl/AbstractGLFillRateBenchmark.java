package com.ankamagames.framework.graphics.engine.benchmark.gl;

import org.apache.log4j.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.states.*;

public abstract class AbstractGLFillRateBenchmark extends AbstractGLBenchmark
{
    protected static Logger m_logger;
    protected static long DURATION;
    protected static float QUAD_LEFT;
    protected static float QUAD_BOTTOM;
    protected static float QUAD_WIDTH;
    protected static float QUAD_HEIGHT;
    protected VertexBufferPCT m_vertexBuffer;
    
    @Override
    public void initialize() {
        super.initialize();
        this.initQuadVertexBuffer(this.m_vertexBuffer = VertexBufferPCT.Factory.newPooledInstance(4));
    }
    
    @Override
    public void cleanUp() {
        this.m_vertexBuffer.removeReference();
        super.cleanUp();
    }
    
    private void initQuadVertexBuffer(final VertexBufferPCT vertexBuffer) {
        int positionIndex = 0;
        final float[] positionBuffer = new float[8];
        positionBuffer[positionIndex++] = AbstractGLFillRateBenchmark.QUAD_LEFT;
        positionBuffer[positionIndex++] = AbstractGLFillRateBenchmark.QUAD_BOTTOM + AbstractGLFillRateBenchmark.QUAD_HEIGHT;
        positionBuffer[positionIndex++] = AbstractGLFillRateBenchmark.QUAD_LEFT;
        positionBuffer[positionIndex++] = AbstractGLFillRateBenchmark.QUAD_BOTTOM;
        positionBuffer[positionIndex++] = AbstractGLFillRateBenchmark.QUAD_LEFT + AbstractGLFillRateBenchmark.QUAD_WIDTH;
        positionBuffer[positionIndex++] = AbstractGLFillRateBenchmark.QUAD_BOTTOM;
        positionBuffer[positionIndex++] = AbstractGLFillRateBenchmark.QUAD_LEFT + AbstractGLFillRateBenchmark.QUAD_WIDTH;
        positionBuffer[positionIndex++] = AbstractGLFillRateBenchmark.QUAD_BOTTOM + AbstractGLFillRateBenchmark.QUAD_HEIGHT;
        vertexBuffer.setPositionBuffer(positionBuffer);
        int textureIndex = 0;
        final float[] texCoordBuffer = new float[8];
        texCoordBuffer[textureIndex++] = 0.0f;
        texCoordBuffer[textureIndex++] = 0.0f;
        texCoordBuffer[textureIndex++] = 0.0f;
        texCoordBuffer[textureIndex++] = 1.0f;
        texCoordBuffer[textureIndex++] = 1.0f;
        texCoordBuffer[textureIndex++] = 1.0f;
        texCoordBuffer[textureIndex++] = 1.0f;
        texCoordBuffer[textureIndex++] = 0.0f;
        vertexBuffer.setTexCoord0Buffer(texCoordBuffer);
        final float[] colorBuffer = new float[16];
        for (int i = 0; i < 16; ++i) {
            colorBuffer[i] = MathHelper.randomFloat();
        }
        vertexBuffer.setColorBuffer(colorBuffer);
    }
    
    protected void prepareViewport() {
        final float halfResX = AbstractGLFillRateBenchmark.QUAD_WIDTH / 2.0f;
        final float halfResY = AbstractGLFillRateBenchmark.QUAD_HEIGHT / 2.0f;
        RenderStateManager.getInstance().applyViewport(this.m_gl, 0, 0, (int)AbstractGLFillRateBenchmark.QUAD_WIDTH, (int)AbstractGLFillRateBenchmark.QUAD_HEIGHT);
        this.m_gl.glOrtho((double)(-halfResX), (double)halfResX, (double)(-halfResY), (double)halfResY, 0.0, 65535.0);
        RenderStateManager.getInstance().applyMatrixMode(this.m_gl, MatrixModes.PROJECTION);
    }
    
    static {
        AbstractGLFillRateBenchmark.m_logger = Logger.getLogger((Class)AbstractGLFillRateBenchmark.class);
        AbstractGLFillRateBenchmark.DURATION = 500000000L;
        AbstractGLFillRateBenchmark.QUAD_LEFT = -512.0f;
        AbstractGLFillRateBenchmark.QUAD_BOTTOM = -512.0f;
        AbstractGLFillRateBenchmark.QUAD_WIDTH = 1024.0f;
        AbstractGLFillRateBenchmark.QUAD_HEIGHT = 1024.0f;
    }
}
