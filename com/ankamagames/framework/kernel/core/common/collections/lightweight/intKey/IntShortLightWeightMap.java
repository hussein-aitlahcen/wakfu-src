package com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey;

public class IntShortLightWeightMap extends AbstractIntKeyLightWeightMap
{
    private short[] m_values;
    
    public IntShortLightWeightMap() {
        this(10);
    }
    
    public IntShortLightWeightMap(final int initialCapacity) {
        super(initialCapacity);
        this.m_values = new short[initialCapacity];
    }
    
    @Override
    public boolean ensureCapacity(final int newCapacity) {
        final int oldCapacity = this.m_indexes.length;
        if (!super.ensureCapacity(newCapacity)) {
            return false;
        }
        final short[] values = this.m_values;
        System.arraycopy(values, 0, this.m_values = new short[newCapacity], 0, oldCapacity);
        return true;
    }
    
    public void put(final int key, final short value) {
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
    
    public short remove(final int key) {
        if (this.m_size == 0) {
            return 0;
        }
        final int index = this.index(key);
        if (index < 0) {
            return 0;
        }
        final short removed = this.m_values[index];
        this.m_indexes[index] = this.m_indexes[this.m_size - 1];
        this.m_values[index] = this.m_values[this.m_size - 1];
        --this.m_size;
        return removed;
    }
    
    public short get(final int key) {
        final int index = this.index(key);
        if (index < 0) {
            return 0;
        }
        return this.m_values[index];
    }
    
    public short getQuickValue(final int index) {
        return this.m_values[index];
    }
    
    @Override
    public void compact() {
        super.compact();
        final short[] values = this.m_values;
        System.arraycopy(values, 0, this.m_values = new short[this.m_size], 0, this.m_size);
    }
}
