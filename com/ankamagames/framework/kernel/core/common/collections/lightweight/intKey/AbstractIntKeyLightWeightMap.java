package com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey;

public abstract class AbstractIntKeyLightWeightMap
{
    protected int[] m_indexes;
    protected int m_size;
    
    protected AbstractIntKeyLightWeightMap(final int initialSize) {
        super();
        this.m_indexes = new int[initialSize];
        this.m_size = 0;
    }
    
    public boolean ensureCapacity(final int newCapacity) {
        if (newCapacity > this.m_indexes.length) {
            final int[] indexes = this.m_indexes;
            System.arraycopy(indexes, 0, this.m_indexes = new int[newCapacity], 0, indexes.length);
            return true;
        }
        return false;
    }
    
    protected final int index(final int key) {
        for (int i = this.m_size - 1; i >= 0; --i) {
            if (key == this.m_indexes[i]) {
                return i;
            }
        }
        return -1;
    }
    
    protected final int insertIndex(final int key) {
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
    
    public final boolean contains(final int key) {
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
    
    public final int getQuickKey(final int index) {
        return this.m_indexes[index];
    }
    
    public void compact() {
        final int[] indexes = this.m_indexes;
        System.arraycopy(indexes, 0, this.m_indexes = new int[this.m_size], 0, this.m_size);
    }
    
    public final int[] keys() {
        final int size = this.size();
        final int[] keys = new int[size];
        for (int i = 0; i < size; ++i) {
            keys[i] = this.getQuickKey(i);
        }
        return keys;
    }
    
    public final boolean isEmpty() {
        return this.size() == 0;
    }
}
