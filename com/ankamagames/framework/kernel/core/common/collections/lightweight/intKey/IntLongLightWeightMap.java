package com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey;

public class IntLongLightWeightMap extends AbstractIntKeyLightWeightMap
{
    private long[] m_values;
    
    public IntLongLightWeightMap() {
        this(10);
    }
    
    public IntLongLightWeightMap(final int initialCapacity) {
        super(initialCapacity);
        this.m_values = new long[initialCapacity];
    }
    
    @Override
    public boolean ensureCapacity(final int newCapacity) {
        final int oldCapacity = this.m_indexes.length;
        if (!super.ensureCapacity(newCapacity)) {
            return false;
        }
        final long[] values = this.m_values;
        System.arraycopy(values, 0, this.m_values = new long[newCapacity], 0, oldCapacity);
        return true;
    }
    
    public void put(final int key, final long value) {
        this.checkCapacity();
        int index = this.insertIndex(key);
        if (index < 0) {
            index = -index - 1;
        }
        else {
            ++this.m_size;
            this.m_indexes[index] = key;
        }
        this.m_values[index] = value;
    }
    
    public long remove(final int key) {
        if (this.m_size == 0) {
            return 0L;
        }
        final int index = this.index(key);
        if (index < 0) {
            return 0L;
        }
        final long removed = this.m_values[index];
        this.m_indexes[index] = this.m_indexes[this.m_size - 1];
        this.m_values[index] = this.m_values[this.m_size - 1];
        --this.m_size;
        return removed;
    }
    
    public long get(final int key) {
        final int index = this.index(key);
        if (index < 0) {
            return 0L;
        }
        return this.m_values[index];
    }
    
    public long getQuickValue(final int index) {
        return this.m_values[index];
    }
    
    @Override
    public void compact() {
        super.compact();
        final long[] values = this.m_values;
        System.arraycopy(values, 0, this.m_values = new long[this.m_size], 0, this.m_size);
    }
}
