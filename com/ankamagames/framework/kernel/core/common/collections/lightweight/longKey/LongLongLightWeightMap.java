package com.ankamagames.framework.kernel.core.common.collections.lightweight.longKey;

public class LongLongLightWeightMap extends AbstractLongKeyLightWeightMap
{
    private long[] m_values;
    
    public LongLongLightWeightMap() {
        this(10);
    }
    
    public LongLongLightWeightMap(final int initialCapacity) {
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
    
    public void put(final long key, final long value) {
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
    
    public long remove(final long key) {
        if (this.m_size == 0) {
            return 0L;
        }
        final int index = this.index(key);
        if (index < 0) {
            return 0L;
        }
        return this.removeQuick(index);
    }
    
    public long removeQuick(final int index) {
        final long removed = this.m_values[index];
        this.m_indexes[index] = this.m_indexes[this.m_size - 1];
        this.m_values[index] = this.m_values[this.m_size - 1];
        --this.m_size;
        return removed;
    }
    
    public long get(final long key) {
        final int index = this.index(key);
        if (index < 0) {
            return 0L;
        }
        return this.m_values[index];
    }
    
    public long getQuickValue(final int index) {
        return this.m_values[index];
    }
}
