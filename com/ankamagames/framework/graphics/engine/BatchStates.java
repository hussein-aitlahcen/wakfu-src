package com.ankamagames.framework.graphics.engine;

import javax.media.opengl.*;
import com.ankamagames.framework.graphics.engine.states.*;
import java.nio.*;

public final class BatchStates
{
    private final VertexBufferPCT m_vertexBuffer;
    private final IndexBuffer m_indexBuffer;
    private GL m_gl;
    private RenderStateManager m_stateManager;
    private int m_mode;
    private int count;
    private int total;
    private int max;
    
    public BatchStates() {
        this(8192, 8192);
    }
    
    public BatchStates(final int maxVertices, final int maxIndices) {
        super();
        this.m_mode = 7;
        this.count = -1;
        this.total = 0;
        this.max = 0;
        this.m_vertexBuffer = VertexBufferPCT.Factory.newPooledInstance(Math.min(maxVertices, maxIndices));
        this.m_indexBuffer = IndexBuffer.INDICES;
    }
    
    public final void begin(final GL gl, final RenderStateManager stateManager) {
        this.m_vertexBuffer.rewind();
        this.m_gl = gl;
        this.m_stateManager = stateManager;
    }
    
    public final void add(final Renderable renderable) {
        if (!renderable.visible()) {
            return;
        }
        final int mode = renderable.getMode();
        if (mode != this.m_mode) {
            this.flush();
            this.m_mode = mode;
        }
        if (!renderable.fill(this.m_vertexBuffer)) {
            this.flush();
            renderable.fill(this.m_vertexBuffer);
        }
        renderable.applyStates(this.m_stateManager);
        ++this.count;
    }
    
    public final void flush() {
        if (this.m_vertexBuffer.getNumVertices() == 0) {
            return;
        }
        final GL gl = this.m_gl;
        RendererType.OpenGL.getRenderer().m_stateManager.setVertexArrayComponents(13);
        gl.glVertexPointer(2, 5126, 0, (Buffer)this.m_vertexBuffer.getPositionBuffer());
        gl.glColorPointer(4, 5126, 0, (Buffer)this.m_vertexBuffer.getColorBuffer());
        gl.glTexCoordPointer(2, 5126, 0, (Buffer)this.m_vertexBuffer.getTexCoord0Buffer());
        gl.glDrawElements(this.m_mode, this.m_vertexBuffer.getNumVertices(), 5123, (Buffer)this.m_indexBuffer.getBuffer());
        this.m_vertexBuffer.rewind();
        if (this.count > this.max) {
            this.max = this.count;
        }
        this.total += this.count;
        this.count = -1;
    }
    
    public final void end() {
        this.flush();
        this.total = 0;
        this.max = 0;
    }
}
