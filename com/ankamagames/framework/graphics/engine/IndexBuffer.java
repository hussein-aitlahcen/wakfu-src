package com.ankamagames.framework.graphics.engine;

import java.nio.*;
import com.ankamagames.framework.graphics.engine.pools.*;

public final class IndexBuffer
{
    public static final IndexBuffer QUAD_INDICES;
    public static final IndexBuffer INDICES;
    public ShortBuffer m_buffer;
    public ShortBufferPool m_bufferPool;
    private final int m_maxIndices;
    
    public IndexBuffer(final short[] indices) {
        super();
        this.m_maxIndices = indices.length;
        this.m_bufferPool = DirectBufferPoolManager.getInstance().getShortBufferPool(this.m_maxIndices * 2);
        this.m_buffer = (ShortBuffer)this.m_bufferPool.getBuffer();
        this.getBuffer().put(indices);
    }
    
    public int getMaxIndices() {
        return this.m_maxIndices;
    }
    
    public int getNumIndices() {
        return this.m_maxIndices;
    }
    
    public ShortBuffer getBuffer() {
        this.m_buffer.rewind();
        return this.m_buffer;
    }
    
    public void release() {
        this.m_bufferPool.release();
        this.m_buffer = null;
    }
    
    static {
        QUAD_INDICES = new IndexBuffer(new short[] { 0, 1, 2, 3 });
        final short[] indices = new short[32767];
        for (short i = 0; i < indices.length; ++i) {
            indices[i] = i;
        }
        INDICES = new IndexBuffer(indices);
    }
}
