package com.ankamagames.framework.kernel.core.common.collections.lightweight.intKey;

public class IntLightWeightSet extends AbstractIntKeyLightWeightMap
{
    public IntLightWeightSet(final int initialSize) {
        super(initialSize);
    }
    
    public IntLightWeightSet() {
        super(10);
    }
    
    public void add(final int key) {
        this.checkCapacity();
        final int index = this.insertIndex(key);
        if (index >= 0) {
            ++this.m_size;
            this.m_indexes[index] = key;
        }
    }
    
    public boolean remove(final int key) {
        if (this.m_size == 0) {
            return false;
        }
        final int index = this.index(key);
        if (index < 0) {
            return false;
        }
        this.m_indexes[index] = this.m_indexes[this.m_size - 1];
        --this.m_size;
        return true;
    }
}
