package com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey;

public class IntIntLightWeightMap extends AbstractIntKeyLightWeightMap
{
    private int[] m_values;
    
    public IntIntLightWeightMap() {
        this(10);
    }
    
    public IntIntLightWeightMap(final int initialCapacity) {
        super(initialCapacity);
        this.m_values = new int[initialCapacity];
    }
    
    @Override
    public boolean ensureCapacity(final int newCapacity) {
        final int oldCapacity = this.m_indexes.length;
        if (!super.ensureCapacity(newCapacity)) {
            return false;
        }
        final int[] values = this.m_values;
        System.arraycopy(values, 0, this.m_values = new int[newCapacity], 0, oldCapacity);
        return true;
    }
    
    public void put(final int key, final int value) {
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
    
    public int remove(final int key) {
        if (this.m_size == 0) {
            return 0;
        }
        final int index = this.index(key);
        if (index < 0) {
            return 0;
        }
        final int removed = this.m_values[index];
        this.m_indexes[index] = this.m_indexes[this.m_size - 1];
        this.m_values[index] = this.m_values[this.m_size - 1];
        --this.m_size;
        return removed;
    }
    
    public int get(final int key) {
        final int index = this.index(key);
        if (index < 0) {
            return 0;
        }
        return this.m_values[index];
    }
    
    public int getQuickValue(final int index) {
        return this.m_values[index];
    }
    
    @Override
    public void compact() {
        super.compact();
        final int[] values = this.m_values;
        System.arraycopy(values, 0, this.m_values = new int[this.m_size], 0, this.m_size);
    }
}
