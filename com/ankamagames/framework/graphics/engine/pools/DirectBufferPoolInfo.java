package com.ankamagames.framework.graphics.engine.pools;

public final class DirectBufferPoolInfo
{
    private int m_bufferSize;
    private int m_buffersCount;
    private DirectBufferPool.Type m_type;
    
    public final int getBufferSize() {
        return this.m_bufferSize;
    }
    
    public final void setBufferSize(final int bufferSize) {
        this.m_bufferSize = bufferSize;
    }
    
    public final int getBuffersCount() {
        return this.m_buffersCount;
    }
    
    public final void setBuffersCount(final int buffersCount) {
        this.m_buffersCount = buffersCount;
    }
    
    public final DirectBufferPool.Type getType() {
        return this.m_type;
    }
    
    public final void setType(final DirectBufferPool.Type type) {
        this.m_type = type;
    }
}
