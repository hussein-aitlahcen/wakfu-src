package com.ankamagames.framework.graphics.engine.pools;

import org.apache.log4j.*;
import java.lang.reflect.*;
import java.util.*;
import gnu.trove.*;

public final class DirectBufferPoolManager
{
    private TIntObjectHashMap<DirectBufferPoolGroup> m_bytePoolGroup;
    private ArrayList<DirectBufferPoolGroup>[] m_poolGroups;
    private static final DirectBufferPoolManager m_instance;
    private static final Logger m_logger;
    private float[] m_maxUsage;
    
    private DirectBufferPoolManager() {
        super();
        this.m_maxUsage = null;
        this.m_bytePoolGroup = null;
        this.m_poolGroups = null;
    }
    
    public static DirectBufferPoolManager getInstance() {
        return DirectBufferPoolManager.m_instance;
    }
    
    public void intialise(final ArrayList<DirectBufferPoolInfo> infos) {
        assert this.m_bytePoolGroup == null : "The DirectBufferPoolManager is already initialised and can't be initialised twice";
        int numGroups = 0;
        for (final DirectBufferPoolInfo info : infos) {
            assert info.getBuffersCount() >= 0 : "bufferCounts can't be less than zero";
            if (info.getBuffersCount() == 0) {
                continue;
            }
            ++numGroups;
        }
        assert numGroups > 0 : "No group created, all buffer counts are null";
        final int numBufferTypes = DirectBufferPool.Type.values().length;
        this.m_bytePoolGroup = new TIntObjectHashMap<DirectBufferPoolGroup>(numGroups * 2);
        this.m_poolGroups = (ArrayList<DirectBufferPoolGroup>[])Array.newInstance(ArrayList.class, numBufferTypes);
        for (int i = 0; i < numBufferTypes; ++i) {
            if (i == DirectBufferPool.Type.ByteBuffer.ordinal()) {
                this.m_poolGroups[i] = null;
            }
            else {
                this.m_poolGroups[i] = new ArrayList<DirectBufferPoolGroup>(numGroups);
            }
        }
        for (final DirectBufferPoolInfo poolInfo : infos) {
            if (poolInfo.getBuffersCount() == 0) {
                continue;
            }
            final DirectBufferPool.Type type = poolInfo.getType();
            if (type == DirectBufferPool.Type.ByteBuffer) {
                this.m_bytePoolGroup.put(poolInfo.getBufferSize(), new DirectBufferPoolGroup(poolInfo));
            }
            else {
                this.m_poolGroups[type.ordinal()].add(new DirectBufferPoolGroup(poolInfo));
            }
        }
        DirectBufferPoolManager.m_logger.info((Object)("DirectBufferPoolManager allocates " + this.getSizeInMemory() / 1024 + "KB"));
    }
    
    public ByteBufferPool getByteBufferPool(final int size) {
        assert this.m_bytePoolGroup != null : "DirectBufferPoolManager is not initialized";
        DirectBufferPoolGroup poolGroup = this.m_bytePoolGroup.get(size);
        if (poolGroup == null) {
            DirectBufferPoolManager.m_logger.error((Object)("Creating byte buffer pool of size " + size));
            final DirectBufferPoolInfo poolInfo = new DirectBufferPoolInfo();
            poolInfo.setBuffersCount(1);
            poolInfo.setBufferSize(size);
            poolInfo.setType(DirectBufferPool.Type.ByteBuffer);
            this.m_bytePoolGroup.put(poolInfo.getBufferSize(), new DirectBufferPoolGroup(poolInfo));
            poolGroup = this.m_bytePoolGroup.get(size);
        }
        final ByteBufferPool byteBufferPool = (ByteBufferPool)poolGroup.getDirectBufferPool();
        assert byteBufferPool != null : "No more free ByteBuffer pools of size " + size;
        return byteBufferPool;
    }
    
    public ShortBufferPool getShortBufferPool(final int size) {
        assert this.m_poolGroups != null : "DirectBufferPoolManager is not initialized";
        assert this.m_poolGroups[DirectBufferPool.Type.ShortBuffer.ordinal()] != null : "No ShortBufferPool initialized";
        return (ShortBufferPool)this.getBuffer(DirectBufferPool.Type.ShortBuffer, size);
    }
    
    public final FloatBufferPool getFloatBufferPool(final int size) {
        assert this.m_poolGroups != null : "DirectBufferPoolManager is not initialized";
        assert this.m_poolGroups[DirectBufferPool.Type.FloatBuffer.ordinal()] != null : "No FloatBufferPool initialized";
        return (FloatBufferPool)this.getBuffer(DirectBufferPool.Type.FloatBuffer, size);
    }
    
    public final int getSizeInMemory() {
        assert this.m_poolGroups != null : "DirectBufferPoolManager is not initialized";
        int memorySize = 0;
        if (this.m_bytePoolGroup != null) {
            final TIntObjectIterator<DirectBufferPoolGroup> poolGroupIter = this.m_bytePoolGroup.iterator();
            while (poolGroupIter.hasNext()) {
                poolGroupIter.advance();
                memorySize += poolGroupIter.value().getSizeInMemory();
            }
        }
        if (this.m_poolGroups != null) {
            for (final ArrayList<DirectBufferPoolGroup> poolGroup : this.m_poolGroups) {
                if (poolGroup != null) {
                    for (final DirectBufferPoolGroup group : poolGroup) {
                        memorySize += group.getSizeInMemory();
                    }
                }
            }
        }
        return memorySize;
    }
    
    public final void printMemoryUsage() {
    }
    
    private DirectBufferPool getBuffer(final DirectBufferPool.Type type, final int size) {
        ArrayList<DirectBufferPoolGroup> poolGroup = null;
        try {
            poolGroup = this.m_poolGroups[type.ordinal()];
        }
        catch (Exception e) {
            DirectBufferPoolManager.m_logger.error((Object)"DEBUG \u00e9cran noir au lancement", (Throwable)new Exception("stacktrace"));
            return null;
        }
        for (int numGroups = poolGroup.size(), i = 0; i < numGroups; ++i) {
            final DirectBufferPoolGroup group = poolGroup.get(i);
            if (group.getSize() >= size) {
                final DirectBufferPool bufferPool = group.getDirectBufferPool();
                if (bufferPool != null) {
                    return bufferPool;
                }
                DirectBufferPoolManager.m_logger.trace((Object)("No enough FloatBuffer pool of size " + size));
            }
        }
        assert false : "No more free " + type + "pools of size " + size;
        return null;
    }
    
    public void printStats() {
        DirectBufferPoolManager.m_logger.warn((Object)"#######################################################");
        DirectBufferPoolManager.m_logger.warn((Object)"##### Pool stats");
        DirectBufferPoolManager.m_logger.warn((Object)"#######################################################");
        final TIntObjectIterator<DirectBufferPoolGroup> groupIt = this.m_bytePoolGroup.iterator();
        while (groupIt.hasNext()) {
            groupIt.advance();
            groupIt.value().printStats();
        }
        for (int i = 0; i < this.m_poolGroups.length; ++i) {
            if (this.m_poolGroups[i] != null) {
                for (final DirectBufferPoolGroup group : this.m_poolGroups[i]) {
                    group.printStats();
                }
            }
        }
    }
    
    static {
        m_instance = new DirectBufferPoolManager();
        m_logger = Logger.getLogger((Class)DirectBufferPoolManager.class);
    }
}
