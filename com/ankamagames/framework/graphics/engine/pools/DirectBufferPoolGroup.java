package com.ankamagames.framework.graphics.engine.pools;

import com.ankamagames.framework.kernel.core.common.collections.*;
import org.apache.log4j.*;
import java.nio.*;
import com.sun.opengl.util.*;

public final class DirectBufferPoolGroup
{
    private DirectBufferPool[] m_directBufferPools;
    private final FreeList m_freeList;
    private final int m_size;
    private final DirectBufferPool.Type m_type;
    private static final Logger m_logger;
    private static final boolean DEBUG = false;
    private final Object m_freelistMutex;
    
    public DirectBufferPoolGroup(final DirectBufferPoolInfo info) {
        super();
        this.m_freelistMutex = new Object();
        this.m_size = info.getBufferSize();
        this.m_type = info.getType();
        final int bufferCount = info.getBuffersCount();
        final Buffer memoryPage = createPage(this.m_size * bufferCount, this.m_type);
        this.m_directBufferPools = new DirectBufferPool[bufferCount];
        for (int i = 0; i < this.m_directBufferPools.length; ++i) {
            final Buffer subBuffer = slicePage(memoryPage, i * this.m_size, this.m_size, this.m_type);
            this.m_directBufferPools[i] = this.createDirectBufferPool(subBuffer, i, this.m_type);
        }
        this.m_freeList = new FreeList(info.getBuffersCount());
    }
    
    public final DirectBufferPool getDirectBufferPool() {
        synchronized (this.m_freelistMutex) {
            int index = this.m_freeList.checkout();
            if (index == -1) {
                int size = this.m_directBufferPools.length;
                int deltaSize;
                if (size < 4096) {
                    deltaSize = size;
                }
                else {
                    deltaSize = 4096;
                }
                size += deltaSize;
                final DirectBufferPool[] bufferPool = new DirectBufferPool[size];
                System.arraycopy(this.m_directBufferPools, 0, bufferPool, 0, this.m_directBufferPools.length);
                final Buffer memoryPage = createPage(this.m_size * deltaSize, this.m_type);
                for (int i = this.m_directBufferPools.length; i < bufferPool.length; ++i) {
                    final int pageOffset = i - this.m_directBufferPools.length;
                    final Buffer subBuffer = slicePage(memoryPage, pageOffset * this.m_size, this.m_size, this.m_type);
                    bufferPool[i] = this.createDirectBufferPool(subBuffer, i, this.m_type);
                }
                assert this.m_freeList.getSize() == this.m_directBufferPools.length;
                this.m_freeList.resize(size);
                this.m_directBufferPools = bufferPool;
                assert this.m_freeList.getSize() == this.m_directBufferPools.length;
                index = this.m_freeList.checkout();
            }
            return this.m_directBufferPools[index];
        }
    }
    
    public final void release(final DirectBufferPool directBufferPool) {
        synchronized (this.m_freelistMutex) {
            this.m_freeList.checkin(directBufferPool.getId());
        }
    }
    
    public final void releaseAllDirectBufferPools() {
        synchronized (this.m_freelistMutex) {
            this.m_freeList.freeAll();
        }
    }
    
    public final int getSize() {
        return this.m_size;
    }
    
    public final int getSizeInMemory() {
        synchronized (this.m_freelistMutex) {
            return this.m_directBufferPools.length * this.m_size;
        }
    }
    
    public final int getSizeInMemoryFree() {
        synchronized (this.m_freelistMutex) {
            return this.m_freeList.getNumFree() * this.m_size;
        }
    }
    
    public final int getSizeInMemoryUsed() {
        synchronized (this.m_freelistMutex) {
            return this.m_freeList.getNumUsed() * this.m_size;
        }
    }
    
    private DirectBufferPool createDirectBufferPool(final Buffer buffer, final int id, final DirectBufferPool.Type bufferType) {
        switch (bufferType) {
            case ByteBuffer: {
                return new ByteBufferPool(buffer, this.m_size, id, this);
            }
            case ShortBuffer: {
                return new ShortBufferPool(buffer, this.m_size, id, this);
            }
            case FloatBuffer: {
                return new FloatBufferPool(buffer, this.m_size, id, this);
            }
            default: {
                assert false : "Buffer type not supported";
                return null;
            }
        }
    }
    
    private static Buffer slicePage(final Buffer memoryPage, final int offset, final int size, final DirectBufferPool.Type bufferType) {
        switch (bufferType) {
            case ByteBuffer: {
                memoryPage.position(offset);
                memoryPage.limit(offset + size);
                final ByteBuffer byteBuffer = (ByteBuffer)memoryPage;
                return byteBuffer.slice();
            }
            case ShortBuffer: {
                memoryPage.position(offset >> 1);
                memoryPage.limit(offset + size >> 1);
                final ShortBuffer shortBuffer = (ShortBuffer)memoryPage;
                return shortBuffer.slice();
            }
            case FloatBuffer: {
                memoryPage.position(offset >> 2);
                memoryPage.limit(offset + size >> 2);
                final FloatBuffer floatBuffer = (FloatBuffer)memoryPage;
                return floatBuffer.slice();
            }
            default: {
                assert false : "Buffer type not supported in slice";
                return null;
            }
        }
    }
    
    private static Buffer createPage(final int pageSize, final DirectBufferPool.Type bufferType) {
        switch (bufferType) {
            case ByteBuffer: {
                return BufferUtil.newByteBuffer(pageSize);
            }
            case ShortBuffer: {
                return BufferUtil.newShortBuffer(pageSize / 2);
            }
            case FloatBuffer: {
                return BufferUtil.newFloatBuffer(pageSize / 4);
            }
            default: {
                assert false : "Buffer type not supported in createPage";
                return null;
            }
        }
    }
    
    public void printStats() {
        synchronized (this.m_freelistMutex) {
            DirectBufferPoolGroup.m_logger.warn((Object)(this.m_type.name() + " size=" + this.m_size + " : " + this.m_freeList.getNumUsed() + "/" + this.m_freeList.getSize()));
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)DirectBufferPoolGroup.class);
    }
}
