package com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey;

public class IntBooleanLightWeightMap extends AbstractIntKeyLightWeightMap
{
    private boolean[] m_values;
    
    public IntBooleanLightWeightMap() {
        this(10);
    }
    
    public IntBooleanLightWeightMap(final int initialCapacity) {
        super(initialCapacity);
        this.m_values = new boolean[initialCapacity];
    }
    
    @Override
    public boolean ensureCapacity(final int newCapacity) {
        final int oldCapacity = this.m_indexes.length;
        if (!super.ensureCapacity(newCapacity)) {
            return false;
        }
        final boolean[] values = this.m_values;
        System.arraycopy(values, 0, this.m_values = new boolean[newCapacity], 0, oldCapacity);
        return true;
    }
    
    public void put(final int key, final boolean value) {
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
    
    public boolean remove(final int key) {
        if (this.m_size == 0) {
            return false;
        }
        final int index = this.index(key);
        if (index < 0) {
            return false;
        }
        final boolean removed = this.m_values[index];
        this.m_indexes[index] = this.m_indexes[this.m_size - 1];
        this.m_values[index] = this.m_values[this.m_size - 1];
        --this.m_size;
        return removed;
    }
    
    public boolean get(final int key) {
        final int index = this.index(key);
        return index >= 0 && this.m_values[index];
    }
    
    public boolean getQuickValue(final int index) {
        return this.m_values[index];
    }
    
    @Override
    public void compact() {
        super.compact();
        final boolean[] values = this.m_values;
        System.arraycopy(values, 0, this.m_values = new boolean[this.m_size], 0, this.m_size);
    }
}
