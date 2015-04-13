package com.ankamagames.framework.graphics.engine.pools;

import java.nio.*;

public final class ShortBufferPool implements DirectBufferPool
{
    private final ShortBuffer m_shortBuffer;
    private final DirectBufferPoolGroup m_directBufferPoolGroup;
    private final int m_id;
    
    public ShortBufferPool(final Buffer buffer, final int size, final int id, final DirectBufferPoolGroup directBufferPoolGroup) {
        super();
        this.m_shortBuffer = (ShortBuffer)buffer;
        this.m_id = id;
        this.m_directBufferPoolGroup = directBufferPoolGroup;
    }
    
    @Override
    public final Buffer getBuffer() {
        return this.m_shortBuffer;
    }
    
    @Override
    public final int getId() {
        return this.m_id;
    }
    
    @Override
    public final void release() {
        assert this.m_directBufferPoolGroup != null;
        this.m_directBufferPoolGroup.release(this);
    }
}
