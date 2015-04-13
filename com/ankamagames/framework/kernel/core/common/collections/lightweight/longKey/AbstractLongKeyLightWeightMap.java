package com.ankamagames.framework.kernel.core.common.collections.lightweight.longKey;

public abstract class AbstractLongKeyLightWeightMap
{
    protected long[] m_indexes;
    protected int m_size;
    
    protected AbstractLongKeyLightWeightMap(final int initialSize) {
        super();
        this.m_indexes = new long[initialSize];
        this.m_size = 0;
    }
    
    public boolean ensureCapacity(final int newCapacity) {
        if (newCapacity > this.m_indexes.length) {
            final int oldCapacity = this.m_indexes.length;
            final long[] indexes = this.m_indexes;
            System.arraycopy(indexes, 0, this.m_indexes = new long[newCapacity], 0, oldCapacity);
            return true;
        }
        return false;
    }
    
    protected int index(final long key) {
        for (int i = this.m_size - 1; i >= 0; --i) {
            if (key == this.m_indexes[i]) {
                return i;
            }
        }
        return -1;
    }
    
    protected int insertIndex(final long key) {
        for (int i = this.m_size - 1; i >= 0; --i) {
            if (key == this.m_indexes[i]) {
                return -i - 1;
            }
        }
        return this.m_size;
    }
    
    protected void checkCapacity() {
        if (this.m_size == this.m_indexes.length) {
            this.ensureCapacity(this.m_indexes.length * 2);
        }
    }
    
    public final boolean contains(final long key) {
        return this.index(key) != -1;
    }
    
    public void reset() {
        this.m_size = 0;
    }
    
    public void clear() {
        this.m_size = 0;
    }
    
    public int size() {
        return this.m_size;
    }
    
    public long getQuickKey(final int index) {
        return this.m_indexes[index];
    }
}
