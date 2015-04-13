package com.ankamagames.framework.kernel.core.common.serialization;

import java.util.*;
import java.nio.*;
import org.jetbrains.annotations.*;

class ByteBufferPool
{
    private static final int POOL_GROWTH_SIZE = 10;
    private final int m_byteBuffersSize;
    private final ArrayList<ByteBuffer> m_activeBuffers;
    private final ArrayList<ByteBuffer> m_idleBuffers;
    private final Object m_poolMutex;
    
    public ByteBufferPool(final int byteBuffersSize) {
        super();
        this.m_activeBuffers = new ArrayList<ByteBuffer>();
        this.m_idleBuffers = new ArrayList<ByteBuffer>();
        this.m_poolMutex = new Object();
        this.m_byteBuffersSize = byteBuffersSize;
    }
    
    @Nullable
    public ByteBuffer borrowBuffer() {
        synchronized (this.m_poolMutex) {
            if (this.m_idleBuffers.isEmpty()) {
                for (int i = 0; i < 10; ++i) {
                    this.m_idleBuffers.add(ByteBuffer.allocate(this.m_byteBuffersSize));
                }
            }
            if (this.m_idleBuffers.isEmpty()) {
                return null;
            }
            final ByteBuffer buffer = this.m_idleBuffers.remove(0);
            this.m_activeBuffers.add(buffer);
            return buffer;
        }
    }
    
    public boolean returnBuffer(final ByteBuffer buffer) {
        if (buffer == null) {
            throw new IllegalArgumentException("buffer = null");
        }
        synchronized (this.m_poolMutex) {
            if (!this.m_activeBuffers.contains(buffer)) {
                return false;
            }
            buffer.clear();
            this.m_activeBuffers.remove(buffer);
            this.m_idleBuffers.add(buffer);
            return true;
        }
    }
}
