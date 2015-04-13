package com.ankamagames.framework.kernel.core.common.collections.lightweight.shortKey;

public abstract class AbstractShortKeyLightWeightMap
{
    protected short[] m_indexes;
    protected int m_size;
    
    public AbstractShortKeyLightWeightMap(final int initialSize) {
        super();
        this.m_indexes = new short[initialSize];
        this.m_size = 0;
    }
    
    public boolean ensureCapacity(final int newCapacity) {
        if (newCapacity > this.m_indexes.length) {
            final short[] indexes = this.m_indexes;
            System.arraycopy(indexes, 0, this.m_indexes = new short[newCapacity], 0, indexes.length);
            return true;
        }
        return false;
    }
    
    protected final int index(final short key) {
        for (int i = this.m_size - 1; i >= 0; --i) {
            if (key == this.m_indexes[i]) {
                return i;
            }
        }
        return -1;
    }
    
    protected final int insertIndex(final short key) {
        for (int i = this.m_size - 1; i >= 0; --i) {
            if (key == this.m_indexes[i]) {
                return -i - 1;
            }
        }
        return this.m_size;
    }
    
    protected final void checkCapacity() {
        if (this.m_size == this.m_indexes.length) {
            this.ensureCapacity(this.m_indexes.length * 2);
        }
    }
    
    public final boolean contains(final short key) {
        return this.index(key) != -1;
    }
    
    public final void reset() {
        this.m_size = 0;
    }
    
    public void clear() {
        this.m_size = 0;
    }
    
    public final int size() {
        return this.m_size;
    }
    
    public final short getQuickKey(final int index) {
        return this.m_indexes[index];
    }
}
